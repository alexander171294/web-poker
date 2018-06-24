package com.tandilserver.poker_lobby.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.tandilserver.poker_lobby.dataBase.domain.Users;
import com.tandilserver.poker_lobby.services.dto.in.UserDataIn;
import com.tandilserver.poker_lobby.services.dto.out.Pong;
import com.tandilserver.poker_lobby.services.dto.out.Response;
import com.tandilserver.poker_lobby.services.dto.out.StatusCodes;
import com.tandilserver.poker_lobby.services.dto.out.UserDataOut;

// esto se mapea en /api/userService
@Controller
@MessageMapping("/userService")
public class UserService {
	
	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	
	// se mapea con /api/userService/ping
	@MessageMapping("/ping")
	public Pong pingRequest() {
		// por ahora dejamos esto para fines de prueba.
		log.debug("PING-PONG call.");
		return new Pong();
	}
	
	// se mapea con /api/userService/registro
	@MessageMapping("/registro")
	public Response registro(UserDataIn userData) {
		Response out = new Response();
		
		
		return out;
	}

	// se mapea con /api/userService/registro
	@MessageMapping("/login")
	public UserDataOut login(UserDataIn userData) {
		UserDataOut udata = new UserDataOut();
		
		udata.statusCode = StatusCodes.OK;
		udata.fichas = 500;
		udata.id_usuario = 100;
		udata.nick = "Albert";
		
		return udata;
	}
	
}
