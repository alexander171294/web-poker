package ar.com.tandilweb.orchestrator.processors;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.com.tandilweb.exchange.BackwardValidationSchema;
import ar.com.tandilweb.exchange.backwardValidation.ChallengeValidation;
import ar.com.tandilweb.exchange.backwardValidation.DataChallenge;
import ar.com.tandilweb.exchange.backwardValidation.Invalid;
import ar.com.tandilweb.exchange.backwardValidation.Unknown;
import ar.com.tandilweb.exchange.roomAuth.Handshake;
import ar.com.tandilweb.orchestrator.persistence.repository.ChallengesRepository;
import ar.com.tandilweb.orchestrator.persistence.repository.UsersRepository;
import ar.com.tandilweb.persistence.domain.Challenges;
import ar.com.tandilweb.persistence.domain.Users;

@Service
public class BackwardValidationProcessor {
	
	@Autowired 
	ChallengesRepository challengeRepository;
	
	@Autowired
	UsersRepository userRepository;
	
	protected static Logger logger = LoggerFactory.getLogger(BackwardValidationProcessor.class);
	
	public BackwardValidationSchema challengeValidation(String schemaBody, Handshake roomData) throws JsonParseException, JsonMappingException, IOException {
		logger.debug("Schema challenge validation");
		ObjectMapper om = new ObjectMapper();
		ChallengeValidation inputSchema = om.readValue(schemaBody, ChallengeValidation.class);
		Challenges challenge = challengeRepository.findById(inputSchema.idChallenge);
		// TODO: improve this to validate ClaimToken
		// challenge doesn't exists:
		if(challenge == null) {
			Unknown uknSchema = new Unknown();
			uknSchema.transactionID = inputSchema.transactionID;
			// send to room
			return uknSchema;
		}
		if(roomData == null || challenge.getId_room() != roomData.serverID) {
			Invalid invalidSchema = new Invalid();
			invalidSchema.transactionID = inputSchema.transactionID;
			// send to room
			return invalidSchema;
		}
		// TODO: *BUG* remove the challenge from database to prevent reuse the old challenge.
		DataChallenge<Users> dch = new DataChallenge<Users>();
		dch.claimToken = challenge.getChallenge();
		dch.idUser = challenge.getId_user();
		dch.transactionID = inputSchema.transactionID;
		dch.userData = userRepository.findById(challenge.getId_user());
		dch.userData.setPassword("*****");
		dch.challengeID = challenge.getChallengeID();
		// send to room.
		return dch;
	}

}
