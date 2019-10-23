package ar.com.tandilweb.room.clientControllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import ar.com.tandilweb.exchange.gameProtocol.SchemaGameProto;
import ar.com.tandilweb.room.handlers.SessionHandler;
import ar.com.tandilweb.room_int.GameCtrlInt;

@Controller
@MessageMapping("/game")
public class GameController {
	
	private static final Logger log = LoggerFactory.getLogger(GameController.class);
	
	@Autowired
	private SessionHandler sessionHandler;
	
	@Autowired
	private GameCtrlInt gameController;
	
	@MessageMapping("/bridge")
	public void bridge(SchemaGameProto message, SimpMessageHeaderAccessor headerAccessor) {
		String sessID = headerAccessor.getSessionId();
		gameController.receivedMessage(message, sessID, sessionHandler);
	}

}
