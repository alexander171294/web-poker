package com.tandilserver.poker_room.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@MessageMapping("/chatService")
public class ChatController {
	
	// Controller for chat.
	private static final Logger log = LoggerFactory.getLogger(ChatController.class);
	
	// mapping from /socket/chatService/ping
	@MessageMapping("/ping")
	public String ping() {
		return "PONG";
	}

}
