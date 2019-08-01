package ar.com.tandilweb.room.clientControllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
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

@Controller
@MessageMapping("/user")
public class AuthController {
	
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	
	@MessageMapping("/authorization")
	public UserAuthSchema authorization(Authorization auth, SimpMessageHeaderAccessor headerAccessor) {
		
		
		// verify if session exists:
		ActiveSession activeSesison = new ActiveSession();
//		return activeSesison;
		
		// if not, create challenge: 
		Challenge challenge = new Challenge();
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
