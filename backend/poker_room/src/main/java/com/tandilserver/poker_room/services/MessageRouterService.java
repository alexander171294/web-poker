package com.tandilserver.poker_room.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MessageRouterService {
	
	private Map<String, SessionData> sessionRegistered = new HashMap<String, SessionData>();
	
	public String registerSession(String sessID, String ip) {
		String internalSessID = UUID.randomUUID().toString();
		SessionData sD = new SessionData();
		sD.clientIP = ip;
		sD.socketSessID = sessID;
		sessionRegistered.put(internalSessID, sD);
		return internalSessID;
	}
	
	public class SessionData {
		public String socketSessID;
		public String clientIP;
	}

}
