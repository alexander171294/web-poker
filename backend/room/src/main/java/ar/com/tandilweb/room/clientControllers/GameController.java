package ar.com.tandilweb.room.clientControllers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	public void bridge(String message, SimpMessageHeaderAccessor headerAccessor) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper om = new ObjectMapper();
		String sessID = headerAccessor.getSessionId();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		SchemaGameProto sgp = om.readValue(message, SchemaGameProto.class);
		gameController.receivedMessage(sgp, message, sessID);
	}

}
