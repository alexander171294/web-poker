package com.tandilserver.poker_room.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class MessageRouterService {
	
	@Autowired
	private SimpMessagingTemplate msgTmp;
	
	private Map<String, SessionData> sessionRegistered = new HashMap<String, SessionData>();
	
	public String registerSession(String sessID, String ip) {
		String internalSessID = UUID.randomUUID().toString();
		SessionData sD = new SessionData();
		sD.clientIP = ip;
		sD.socketSessID = sessID;
		sessionRegistered.put(internalSessID, sD);
		return internalSessID;
	}
	
	public void sendToClient(String internalSessID, String direction, Object message) {
		SessionData sD = sessionRegistered.get(internalSessID);
		msgTmp.convertAndSendToUser(sD.socketSessID, direction, message, createHeaders(sD.socketSessID));
	}
	
	public void sendToClientInterceptor(String internalSessID, String direction, Object message) {
		this.sendToClient(internalSessID, "/clientInterceptor"+direction, message);
	}

	private MessageHeaders createHeaders(String sessionId) {
	    SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
	    headerAccessor.setSessionId(sessionId);
	    headerAccessor.setLeaveMutable(true);
	    return headerAccessor.getMessageHeaders();
	}
	
	public class SessionData {
		public String socketSessID;
		public String clientIP;
	}

}
