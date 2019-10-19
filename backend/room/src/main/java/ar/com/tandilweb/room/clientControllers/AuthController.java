package ar.com.tandilweb.room.clientControllers;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.com.tandilweb.exchange.backwardValidation.ChallengeValidation;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.accessing.SelectPosition;
import ar.com.tandilweb.exchange.userAuth.ActiveSession;
import ar.com.tandilweb.exchange.userAuth.Authorization;
import ar.com.tandilweb.exchange.userAuth.BackwardValidation;
import ar.com.tandilweb.exchange.userAuth.Challenge;
import ar.com.tandilweb.exchange.userAuth.types.ChallengeActions;
import ar.com.tandilweb.room.handlers.GameHandler;
import ar.com.tandilweb.room.handlers.RoomHandler;
import ar.com.tandilweb.room.handlers.SessionHandler;
import ar.com.tandilweb.room.handlers.dto.UserData;
import ar.com.tandilweb.room.handlers.dto.UserDataStatus;
import ar.com.tandilweb.room.orchestratorBridge.OrchestratorThread;

@Controller
@MessageMapping("/user")
public class AuthController {
	
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	private SessionHandler sessionHandler;
	
	@Autowired
	private RoomHandler roomHandler;
	
	@Autowired
	private OrchestratorThread orchestrator;
	
	@Autowired
	private GameHandler gameHandler;
	
	@MessageMapping("/authorization")
	@SendToUser("/AuthController/challenge")
	public Challenge authorization(Authorization auth, SimpMessageHeaderAccessor headerAccessor) {
		String sessID = headerAccessor.getSessionId();
		log.debug("New session: " + sessID);
		if(sessionHandler.isActiveSessionForUser(auth.userID)) {
			ActiveSession activeSession = new ActiveSession();
			sessionHandler.sendToUserID("/AuthController/activeSession", auth.userID, activeSession);
		}
		Challenge challenge = new Challenge();
		challenge.action = ChallengeActions.LOGIN;
		challenge.roomID = roomHandler.getRoomID();
		challenge.claimToken = UUID.randomUUID().toString();
		UserData userData = new UserData();
		userData.lastChallenge = challenge;
		userData.sessID = sessID;
		userData.userID = auth.userID;
		userData.status = UserDataStatus.PENDING;
		sessionHandler.assocSessionWithUserData(sessID, userData);
		//sessionHandler.sendToSessID("/AuthController/challenge", sessID, challenge);
		return challenge;
	}
	
	@MessageMapping("/backwardValidation")
	public void backwardValidation(BackwardValidation bV, SimpMessageHeaderAccessor headerAccessor) {
		String sessID = headerAccessor.getSessionId();
		// send to orchestrator:
		ChallengeValidation challengeValidation = new ChallengeValidation();
		challengeValidation.idChallenge = bV.idChallenge;
		challengeValidation.transactionID = UUID.randomUUID().toString();
		sessionHandler.getUserDataBySession(sessID).transactionID = challengeValidation.transactionID;
		ObjectMapper om = new ObjectMapper();
		try {
			orchestrator.sendDataToServer(om.writeValueAsString(challengeValidation));
//			server response:
//			eppr/backward-validation::unknown
//			eppr/backward-validation::invalid
//			eppr/backward-validation::dataChallenge
		} catch (JsonProcessingException e) {
			log.error("Error processing challenge validation", e);
		}
	}
	
	@MessageMapping("/selectPosition")
	public void selectPosition(SelectPosition selectedPosition, SimpMessageHeaderAccessor headerAccessor){
		String sessID = headerAccessor.getSessionId();
		gameHandler.sitFlow(selectedPosition.position, sessionHandler.getUserDataBySession(sessID));
	}
	
	// TODO: make user-deposit schema implementation.
	
	
	// TODO: make leave schema implementation.
	

}
