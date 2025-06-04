
package com.test.demo;

import java.util.List;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

	@Autowired
	ChatModel chatmodel;

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping(value = "/user/chat", produces = "application/json")
	public String hello(@RequestParam String question) {

		System.out.println("user Question:" + question);
		// Build a structured Prompt using System + User messages
		Prompt prompt = new Prompt(List.of(new SystemMessage("""
				    You are Sam, a polite and knowledgeable AI assistant.
				    Then, answer the user's question politely and clearly.
				    Always be respectful and never respond rudely, even if provoked.
				"""), new UserMessage(question)));

		ChatResponse response = chatmodel.call(prompt);
		return response.getResult().getOutput().getText();

	}
}
