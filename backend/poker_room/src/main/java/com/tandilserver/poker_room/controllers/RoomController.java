package com.tandilserver.poker_room.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@MessageMapping("/roomService")
public class RoomController {

	// Room general information as Sits and General Health Status.
	private static final Logger log = LoggerFactory.getLogger(RoomController.class);

	// mapping from /stompApi/roomService/ping
	@MessageMapping("/ping")
	public String ping() {
		return "PONG";
	}

}
