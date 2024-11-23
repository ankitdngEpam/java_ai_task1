package com.epam.training.gen.ai.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.epam.training.gen.ai.dto.UserInput;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;

import reactor.core.publisher.Mono;

@Service
public  interface AiService {
	
	public abstract Mono<List<ChatMessageContent<?>>> getAiResponse(UserInput input) throws ServiceNotFoundException;

}
