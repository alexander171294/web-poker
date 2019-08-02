package ar.com.tandilweb.room.clientControllers;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import ar.com.tandilweb.exchange.UserAuthSchema;
import ar.com.tandilweb.exchange.backwardValidation.ChallengeValidation;
import ar.com.tandilweb.exchange.userAuth.ActiveSession;
import ar.com.tandilweb.exchange.userAuth.Authorization;
import ar.com.tandilweb.exchange.userAuth.BackwardValidation;
import ar.com.tandilweb.exchange.userAuth.BadRequest;
import ar.com.tandilweb.exchange.userAuth.Challenge;
import ar.com.tandilweb.exchange.userAuth.FullRejected;
import ar.com.tandilweb.exchange.userAuth.Kicked;
import ar.com.tandilweb.exchange.userAuth.Rejected;
import ar.com.tandilweb.exchange.userAuth.Validated;
import ar.com.tandilweb.exchange.userAuth.types.ChallengeActions;
import ar.com.tandilweb.room.handlers.RoomHandler;
import ar.com.tandilweb.room.handlers.SessionHandler;
import ar.com.tandilweb.room.handlers.dto.UserData;
import ar.com.tandilweb.room.handlers.dto.UserDataStatus;

@Controller
@MessageMapping("/user")
public class AuthController {
	
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	private SessionHandler sessionHandler;
	
	@Autowired
	private RoomHandler roomHandler;
	
	@MessageMapping("/authorization")
	@SendToUser("/userAuth/challenge")
	public UserAuthSchema authorization(Authorization auth, SimpMessageHeaderAccessor headerAccessor) {
		String sessID = headerAccessor.getSessionId();
		log.debug("New session: " + sessID);
		if(sessionHandler.isActiveSessionForUser(auth.userID)) {
			ActiveSession activeSession = new ActiveSession();
			sessionHandler.sendToUserID("/userAuth/activeSession", auth.userID, activeSession);
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
		return challenge;
	}
	
	@MessageMapping("/backwardValidation")
	public UserAuthSchema backwardValidation(BackwardValidation bV, SimpMessageHeaderAccessor headerAccessor) {
		
		// send to orchestrator:
		ChallengeValidation challengeValidation = new ChallengeValidation();
		
//		server response:
//			eppr/backward-validation::unknown
//			eppr/backward-validation::invalid
//			eppr/backward-validation::dataChallenge
		
		// if unknown or invalid:
		Rejected reject = new Rejected();
		//return reject;
		
		// if dataChallange:
		BadRequest badReq = new BadRequest();
		FullRejected frej = new FullRejected();
		Validated validated = new Validated();
		Kicked kicked = new Kicked();
		
		return kicked;
	}
	
	// TODO: make user-deposit schema implementation.
	
	// TODO: make leave schema implementation.

}
