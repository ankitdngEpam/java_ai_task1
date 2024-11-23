package com.epam.training.gen.ai.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.epam.training.gen.ai.dto.UserInput;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;

import reactor.core.publisher.Mono;

@Component
public class AzureOpenAiServiceImpl implements AiService {

//Getting azure AI configurations
	@Value("${client-openai-key}")
	private String azureKey;

	@Value("${client-openai-endpoint}")
	private String endpoint;

	@Value("${client-openai-deployment-name}")
	private String modelId;

	@Value("${client-openai-system-message}")
	private String systemMessage;

	public Mono<List<ChatMessageContent<?>>> getAiResponse(UserInput input) throws ServiceNotFoundException {

		OpenAIAsyncClient client = new OpenAIClientBuilder().credential(new AzureKeyCredential(azureKey))
				.endpoint(endpoint).buildAsyncClient();

		// Create the chat completion service
		ChatCompletionService openAIChatCompletion = OpenAIChatCompletion.builder().withOpenAIAsyncClient(client)
				.withModelId(modelId).build();

		// Initialize the kernel
		Kernel kernel = Kernel.builder().withAIService(ChatCompletionService.class, openAIChatCompletion).build();

		ChatCompletionService chatCompletionService = kernel.getService(ChatCompletionService.class);

		ChatHistory chatHistory = new ChatHistory();
		chatHistory.addUserMessage(input.getInput());

		chatHistory.addSystemMessage(systemMessage);

		InvocationContext optionalInvocationContext = null;

		Mono<List<ChatMessageContent<?>>> response = chatCompletionService.getChatMessageContentsAsync(chatHistory,
				kernel, optionalInvocationContext);

		return response;
	};

};
