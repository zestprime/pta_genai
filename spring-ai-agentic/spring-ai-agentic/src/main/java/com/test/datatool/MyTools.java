package com.test.datatool;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MyTools {

	  @Tool(description = "Who am i?")
	    String getMyDetails() {
	    	System.out.println("getMyDetails .. called ");
	        return "You are Sam, a polite and knowledgeable AI assistant.\r\n"
	        		+ "			    Then, answer the user's question politely and clearly based on Tools response.If no response return not found data.\r\n"
	        		+ "			    Always be respectful and never respond rudely, even if provoked";
	    }

		@Tool(description = "Get the current date and time in the user's timezone")
		String getCurrentDateTime() {
			System.out.println("getCurrentDateTime .. called ");
			return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
		}

		@Tool(description = "Set a user alarm for the given time, provided in ISO-8601 format")
		String  setAlarm(String hours) {
			System.out.println("setAlarm .. called :"+hours);
			LocalDateTime alarmTime = LocalDateTime.parse(hours, DateTimeFormatter.ISO_DATE_TIME);
			System.out.println("Alarm set for " + alarmTime);
			return alarmTime.toString();
		}
		
		@Tool(description = "get the City temprature based CityName provided as input")
		String getTemprature(String cityName) {
			System.out.println("getTemprature .. called :"+cityName);
			return "The temprature in "+cityName+" is 28°C " +  LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
		}
		
		@Tool(description = "get the Current Stock price based stockname provided as input")
		String getStockPrice(String stockname) {
			System.out.println("getStockPrice .. called :"+stockname);
			return "Todays Stock Price of "+stockname+" is 1,956.90 INR";
		}
		
		@Tool(description = "get the knowledge sharing sessions planned list")
		String getKnowledgeSharingList(String courceName) {
			
			return "Current Knowledge sharing sessions are planned list Core java,React JS,Generative AI and Oracle";
		}
}