package com.epam.training.gen.ai.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epam.training.gen.ai.dto.AiResponse;
import com.epam.training.gen.ai.dto.UserInput;
import com.epam.training.gen.ai.service.AiService;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/chat")
public class GenericResponseController {
	
	private final AiService aiService ;
	
	public GenericResponseController(AiService aiService) {
		
		this.aiService = aiService;
	}
	
	

	@PostMapping("/prompt")
	public Mono<AiResponse> getResponse(@RequestBody UserInput input) throws ServiceNotFoundException {

		Mono<List<ChatMessageContent<?>>> response = aiService.getAiResponse(input);

		return response.map(chatMessageContents -> {
			StringBuilder aiAnswer = new StringBuilder();
			chatMessageContents.forEach(chatMessage -> aiAnswer.append(chatMessage.toString()).append("\n"));
			return new AiResponse(aiAnswer.toString().trim());
		}).onErrorResume(error -> {
			// Handle errors and return a fallback JSON response
			System.err.println("Error occurred: " + error.getMessage());
			return Mono.just(new AiResponse("Error occurred while fetching chat messages."));
		});

	}



	
}
