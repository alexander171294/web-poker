package com.tandilserver.poker_room.websockets;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SubProtocolWebSocketHandler;

public class WebSocketMonitor extends SubProtocolWebSocketHandler {
	
	//@Autowired
	//private ServerManager serverManager;

	public WebSocketMonitor(MessageChannel clientInboundChannel, SubscribableChannel clientOutboundChannel) {
	super(clientInboundChannel, clientOutboundChannel);
	}

	private static final Logger log = LoggerFactory.getLogger(WebSocketMonitor.class);
    private List<WebSocketSession> sessions = new ArrayList<WebSocketSession>();
    

    @Override 
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    	log.debug("SESSION CONNECTED ID["+session.getId()+"]");
        sessions.add(session);
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    	log.debug("SESSION CLOSED ID["+session.getId()+"]");
    	//serverManager.unRegister(session.getId());
        sessions.remove(session);
        super.afterConnectionClosed(session, status);
    }

//    public void handleTextMessage(WebSocketSession session, TextMessage message) {
//        // clientsession = session;
//        // Send individual or broadcast messages here ...
//        //session.sendMessage(new TextMessage(textMessage + "!"));
//    }

}