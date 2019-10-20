package ar.com.tandilweb.room.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import ar.com.tandilweb.room.handlers.dto.UserData;

@Service
public class SessionHandler {
	
	private static final Logger log = LoggerFactory.getLogger(SessionHandler.class);

	private Map<String, UserData> userAssociation = new HashMap<String, UserData>(); 
	
	@Autowired
	private SimpMessagingTemplate msgTmp;
	
	public UserData getUserDataBySession(String session) {
		return userAssociation.get(session);
	}
	
	public boolean isActiveSessionForUser(long userID) {
		for(String sessID: userAssociation.keySet()) {
			if(userAssociation.get(sessID).userID == userID) {
				return true;
			}
		}
		return false;
	}
	
	public UserData getUserDataFromActiveSessionForUser(long userID) {
		for(String sessID: userAssociation.keySet()) {
			if(userAssociation.get(sessID).userID == userID) {
				return userAssociation.get(sessID);
			}
		}
		return null;
	}
	
	public List<UserData> getAnotherSessions(String meSession, long userID) {
		List<UserData> out = new ArrayList<UserData>();
		for(String sessID: userAssociation.keySet()) {
			UserData userData = userAssociation.get(sessID);
			if(userData.userID == userID && !userData.sessID.equals(meSession)) {
				out.add(userData);
			}
		}
		return out;
	}
	
	public String getSessionByTransactionID(String transactionID) {
		for(String sessID: userAssociation.keySet()) {
			if(userAssociation.get(sessID).transactionID != null && 
			   userAssociation.get(sessID).transactionID.equals(transactionID)) {
				return sessID;
			}
		}
		return null;
	}
	
	public String getActiveSessionForUser(long userID) {
		for(String sessID: userAssociation.keySet()) {
			if(userAssociation.get(sessID).userID == userID) {
				return sessID;
			}
		}
		return null;
	}
	
	public void assocSessionWithUserData(String sessID, UserData userData) {
		userAssociation.put(sessID, userData);
	}
	
	public void sendToSessID(String direction, String sessionID, Object payload) {
		log.debug("Sending message to a sessionID: "+sessionID);
		msgTmp.convertAndSendToUser(sessionID, direction, payload, createHeaders(sessionID));
	}
	
	public void sendToAll(String direction, Object payload) {
		msgTmp.convertAndSend(direction, payload);
	}
	
	public void remove(String sessionID) {
		userAssociation.remove(sessionID);
		// TODO: close connection.
	}
	
	public void sendToUserID(String direction, long userID, Object payload) {
		sendToSessID(direction, getActiveSessionForUser(userID), payload);
	}
	
	private MessageHeaders createHeaders(String sessionId) {
	    SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
	    headerAccessor.setSessionId(sessionId);
	    headerAccessor.setLeaveMutable(true);
	    return headerAccessor.getMessageHeaders();
	}
}
