package com.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.agent.MasterAgent;

@RestController
public class RAGController {

	@Autowired
	private final MasterAgent masterAgent;

	public RAGController(MasterAgent masterAgent) {
		this.masterAgent = masterAgent;
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping(value = "/user/chat", produces = "application/json")
	public String ask(@RequestParam String question) {
		return masterAgent.handle(question);
	}
}