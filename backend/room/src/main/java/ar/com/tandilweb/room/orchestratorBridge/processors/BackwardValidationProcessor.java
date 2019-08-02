package ar.com.tandilweb.room.orchestratorBridge.processors;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.com.tandilweb.exchange.backwardValidation.DataChallenge;
import ar.com.tandilweb.exchange.backwardValidation.Invalid;
import ar.com.tandilweb.exchange.backwardValidation.Unknown;
import ar.com.tandilweb.room.handlers.SessionHandler;

@Component
public class BackwardValidationProcessor extends OrchestratorGenericProcessor {
	
	public static Logger logger = LoggerFactory.getLogger(BackwardValidationProcessor.class);
	
	@Autowired
	private SessionHandler sessionHandler;

	public void processDataChallengeSchema(String schemaBody) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			DataChallenge dataResponse = objectMapper.readValue(schemaBody, DataChallenge.class);
//			// if dataChallange:
//			BadRequest badReq = new BadRequest();
//			FullRejected frej = new FullRejected();
//			Validated validated = new Validated();
//			Kicked kicked = new Kicked();
		} catch (IOException e) {
			logger.error("Error processing schema from orchestrator", e);
		}
	}

	public void processInvalidSchema(String schemaBody) throws JsonProcessingException {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			Invalid invalidResponse = objectMapper.readValue(schemaBody, Invalid.class);
			
			String sessionID = sessionHandler.getSessionByTransactionID(invalidResponse.transactionID);
			// if unknown or invalid:
//			Rejected reject = new Rejected();
//			//return reject;
		} catch (IOException e) {
			logger.error("Error processing schema from orchestrator", e);
		}
	}

	public void processUnknownSchema(String schemaBody) throws JsonProcessingException {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			Unknown unknownResponse = objectMapper.readValue(schemaBody, Unknown.class);
			// if unknown or invalid:
//			Rejected reject = new Rejected();
//			//return reject;
		} catch (IOException e) {
			logger.error("Error processing schema from orchestrator", e);
		}
	}
}
