package ar.com.tandilweb.orchestrator.processors;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.com.tandilweb.exchange.roomAuth.Handshake;
import ar.com.tandilweb.exchange.roomAuth.SignupData;
import ar.com.tandilweb.orchestrator.adapters.RoomAuthService;
import ar.com.tandilweb.orchestrator.handlers.LoginResponse;
import ar.com.tandilweb.orchestrator.handlers.RoomHandlerThread;

@Service
public class RoomAuthProcessor {
	
	@Autowired
	RoomAuthService roomAuthSrv;
	
	protected static Logger logger = LoggerFactory.getLogger(RoomAuthProcessor.class);
	
	public boolean processHandshakeSchema(String schemaBody, RoomHandlerThread roomThread) throws JsonParseException, JsonMappingException, IOException {
		logger.debug("Schema body Handshake");
		ObjectMapper om = new ObjectMapper();
		Handshake handshakeSchema = om.readValue(schemaBody, Handshake.class);
		roomThread.setHandshakeSchema(handshakeSchema);
		LoginResponse loginResponse = roomAuthSrv.handshakeValidate(handshakeSchema);
		roomThread.sendToClient(om.writeValueAsString(loginResponse.response));
		return loginResponse.logged;
	}
	
	public boolean processSignupDataSchema(String schemaBody, Handshake handshakeSchema, RoomHandlerThread roomThread) throws JsonParseException, JsonMappingException, IOException {
		logger.debug("Schema body Handshake");
		ObjectMapper om = new ObjectMapper();
		SignupData inputSchema = om.readValue(schemaBody, SignupData.class);
		LoginResponse loginResponse = roomAuthSrv.signupDataValidate(inputSchema, handshakeSchema);
		roomThread.sendToClient(om.writeValueAsString(loginResponse.response));
		return loginResponse.logged;
	}

}
