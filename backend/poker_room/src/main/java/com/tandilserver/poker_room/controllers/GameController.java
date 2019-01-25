package com.tandilserver.poker_room.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@MessageMapping("/gameService")
public class GameController {
	
	// controller for game purposes.
	private static final Logger log = LoggerFactory.getLogger(GameController.class);
	
	// mapping from /socket/gameService/ping
	@MessageMapping("/ping")
	public String ping() {
		return "PONG";
	}
}
