package com.tandilserver.poker_lobby.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.tandilserver.poker_lobby.services.dto.out.Pong;

// esto se mapea en /api/clientInterceptorService
@Controller
@MessageMapping("/clientInterceptorService")
public class UserService {
	
	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	
	public Pong pingRequest() {
		// por ahora dejamos esto para fines de prueba.
		log.debug("PING-PONG call.");
		return new Pong();
	}

}
