package com.tandilserver.poker_room.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tandilserver.poker_intercom.handshake.UserDataVerifier;
import com.tandilserver.poker_room.controllers.dto.in.DeviceClientData;
import com.tandilserver.poker_room.registerService.RegisterServiceThread;
import com.tandilserver.poker_room.services.MessageRouterService;
import com.tandilserver.poker_room.services.UserService;
import com.tandilserver.poker_room.services.dto.UserData;

@Controller
@MessageMapping("/roomService")
public class RoomController {

	// Room general information as Sits and General Health Status.
	private static final Logger log = LoggerFactory.getLogger(RoomController.class);
	
	@Autowired
	private MessageRouterService msgRouterServ;
	
	@Autowired
	private UserService usrServ;
	
	@Autowired
	private RegisterServiceThread regServThread;

	// mapping from /stompApi/roomService/ping
	@MessageMapping("/ping")
	public String ping() {
		return "PONG";
	}
	
	@MessageMapping("/login")
	public void login(DeviceClientData data, SimpMessageHeaderAccessor headerAccessor) {
		String sessID = headerAccessor.getSessionId();
		String ip = headerAccessor.getSessionAttributes().get("IP").toString();
		log.debug("Login: " + sessID + "/" + ip);
		
		UserData userData = new UserData();
		userData.messageRouterSessionUUID = msgRouterServ.registerSession(sessID, ip);
		userData.socketSessionUUID = sessID;
		userData.id_usuario = data.id_usuario;
		userData.nick = data.nick;
		userData.signature = data.signature;
		userData.signup_date = data.signup_date;
		userData.verified = false;
		userData.playing = false;
		String userSessionUUID = usrServ.registerViewer(userData);
		ObjectMapper oM = new ObjectMapper();
		UserDataVerifier uDV = new UserDataVerifier();
		uDV.id_usuario = userData.id_usuario;
		uDV.nick = userData.nick;
		uDV.signature = userData.signature;
		uDV.signup_date = userData.signup_date;
		uDV.userSessionUUID = userSessionUUID;
		String udvSerialized;
		try {
			udvSerialized = oM.writeValueAsString(uDV);
			regServThread.sendDataToServer(udvSerialized);
		} catch (JsonProcessingException e) {
			log.error("Cant parse User Data Verifier", e);
		}
	}

}
