package ar.com.tandilweb.room.orchestratorBridge.processors;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.com.tandilweb.exchange.UserAuthSchema;
import ar.com.tandilweb.exchange.backwardValidation.DataChallenge;
import ar.com.tandilweb.exchange.backwardValidation.Invalid;
import ar.com.tandilweb.exchange.backwardValidation.Unknown;
import ar.com.tandilweb.exchange.userAuth.BadRequest;
import ar.com.tandilweb.exchange.userAuth.Kicked;
import ar.com.tandilweb.exchange.userAuth.Rejected;
import ar.com.tandilweb.exchange.userAuth.Validated;
import ar.com.tandilweb.room.handlers.SessionHandler;
import ar.com.tandilweb.room.handlers.dto.UserData;
import ar.com.tandilweb.room.handlers.dto.UserDataStatus;
import ar.com.tandilweb.room.orchestratorBridge.processors.dtoDB.Users;

@Component
public class BackwardValidationProcessor extends OrchestratorGenericProcessor {
	
	public static Logger logger = LoggerFactory.getLogger(BackwardValidationProcessor.class);
	
	@Autowired
	private SessionHandler sessionHandler;

	public void processDataChallengeSchema(String schemaBody) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			DataChallenge<Users> dataResponse = objectMapper.readValue(schemaBody, new TypeReference<DataChallenge<Users>>(){});
			String sessionID = sessionHandler.getSessionByTransactionID(dataResponse.transactionID);
			UserData userData = sessionHandler.getUserDataBySession(sessionID);
			UserAuthSchema out;
			if(userData.userID == dataResponse.idUser) {
				if(dataResponse.claimToken.equals(userData.lastChallenge.claimToken)) {
					out = new Validated();
					userData.status = UserDataStatus.ACTIVED;
					List<UserData> anotherSessions = sessionHandler.getAnotherSessions(sessionID, userData.userID);
					Kicked kicked = new Kicked();
					if(anotherSessions.size() > 0) {
						for(UserData anotherSession: anotherSessions) {
							anotherSession.status = UserDataStatus.KICKED;
							sessionHandler.sendToSessID("/userAuth/kick", anotherSession.sessID, kicked);
							sessionHandler.remove(anotherSession.sessID);
						}
					}
				} else {
					// TODO: check fails and send full rejected and block ip
					// FullRejected frej = new FullRejected();
					out = new BadRequest();
				}
			} else {
				// TODO: check fails and send full rejected and block ip
				// FullRejected frej = new FullRejected();
				out = new BadRequest();
			}
			sessionHandler.sendToSessID("/AuthController/response", sessionID, out);
		} catch (IOException e) {
			logger.error("Error processing schema from orchestrator", e);
		}
	}

	public void processInvalidSchema(String schemaBody) throws JsonProcessingException {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			Invalid invalidResponse = objectMapper.readValue(schemaBody, Invalid.class);
			String sessionID = sessionHandler.getSessionByTransactionID(invalidResponse.transactionID);
			Rejected reject = new Rejected();
			sessionHandler.sendToSessID("/AuthController/rejected", sessionID, reject);
		} catch (IOException e) {
			logger.error("Error processing schema from orchestrator", e);
		}
	}

	public void processUnknownSchema(String schemaBody) throws JsonProcessingException {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			Unknown unknownResponse = objectMapper.readValue(schemaBody, Unknown.class);
			String sessionID = sessionHandler.getSessionByTransactionID(unknownResponse.transactionID);
			if(sessionID == null) {
				logger.error("Failed to recover Session ID in Transaction ID: " + unknownResponse.transactionID);
			}
			Rejected reject = new Rejected();
			sessionHandler.sendToSessID("/AuthController/rejected", sessionID, reject);
		} catch (IOException e) {
			logger.error("Error processing schema from orchestrator", e);
		}
	}
}
