package com.test.agent;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.test.datatool.MyTools;

@Component
public class MasterAgent {

	@Autowired
	ChatModel chatModel;

	@Autowired
	private MyTools myTools;

	public String handle(String question) {

		System.out.println("Master Agent received User question:"+question);

		String response = ChatClient.create(chatModel)
		        .prompt(question)
		        .tools(myTools)
		        .call()
		        .content();
	return response;
	}
}
