package com.test.spring_ai_rag.controller;

import jakarta.annotation.PostConstruct;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class RAGController {

	private final ChatClient chatClient;

	private final EmbeddingModel embeddingModel;

	private SimpleVectorStore vectorStore;

	private String template;

	@Value("${file.path}")
	private Resource resource;

	public RAGController(EmbeddingModel embeddingModel, ChatClient.Builder chatClientBuilder) {
		this.embeddingModel = embeddingModel;
		this.chatClient = chatClientBuilder.build();
	}

	@PostConstruct
	public void init() {
		vectorStore = SimpleVectorStore.builder(embeddingModel).build();
		
		String text = null;
		try (PDDocument document = PDDocument.load(resource.getFile())) {
			PDFTextStripper pdfStripper = new PDFTextStripper();
			text = pdfStripper.getText(document);
			System.out.println(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("resource loading start...............");

		Document document = new Document(text);
		List<Document> documentList = List.of(document);

		vectorStore.accept(documentList);
		System.out.println("resource loading done...............");
		template = """
					You are Sam, a polite and knowledgeable AI assistant.
				    Then, answer the user's question politely and clearly.
				    Always be respectful and never respond rudely, even if provoked."

				KNOWLEDGE BASE
				---
				{documents}
				""";
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping(value = "/user/chat", produces = "application/json")
	public String rag(@RequestParam String question) {
		System.out.println("User question:"+question);
		// Retrieval
		String relevantDocs = vectorStore.similaritySearch(question).stream().map(Document::getText)
				.collect(Collectors.joining());

		// Augmented
		Message systemMessage = new SystemPromptTemplate(template).createMessage(Map.of("documents", relevantDocs));

		// Generation
		Message userMessage = new UserMessage(question);
		Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
		ChatClient.CallResponseSpec res = chatClient.prompt(prompt).call();

		System.out.println("Response send ..........");
		return res.content();
	}

}
