package ar.com.tandilweb.room.orchestratorBridge.processors;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.com.tandilweb.exchange.serverRecording.InvalidDeposit;
import ar.com.tandilweb.exchange.serverRecording.SuccessDeposit;
import ar.com.tandilweb.room.handlers.GameHandler;
import ar.com.tandilweb.room.handlers.SessionHandler;

@Component
public class ServerRecordingProcessor extends OrchestratorGenericProcessor {
	
	@Autowired
	private GameHandler gameHandler;
	
	public static Logger logger = LoggerFactory.getLogger(ServerRecordingProcessor.class);
	
	@Autowired
	private SessionHandler sessionHandler;
	
	public void processSuccessDeposit(String schemaBody) throws JsonProcessingException {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			SuccessDeposit dataResponse = objectMapper.readValue(schemaBody, SuccessDeposit.class);
			ar.com.tandilweb.exchange.clientOperations.SuccessDeposit out = new ar.com.tandilweb.exchange.clientOperations.SuccessDeposit();
			out.chips = dataResponse.depositedChips;
			gameHandler.addChipsToUser(dataResponse.userID, dataResponse.depositedChips, dataResponse.chips);
			sessionHandler.sendToUserID("GameController/successDeposit", dataResponse.userID, out);
		} catch (IOException e) {
			logger.error("Error processing schema from orchestrator", e);
		}
	}
	
	public void processInvalidDeposit(String schemaBody) throws JsonProcessingException {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			InvalidDeposit dataResponse = objectMapper.readValue(schemaBody, InvalidDeposit.class);
			ar.com.tandilweb.exchange.clientOperations.InvalidDeposit out = new ar.com.tandilweb.exchange.clientOperations.InvalidDeposit();
			sessionHandler.sendToUserID("GameController/invalidDeposit", dataResponse.userID, out);
		} catch (IOException e) {
			logger.error("Error processing schema from orchestrator", e);
		}
		
	}

}
