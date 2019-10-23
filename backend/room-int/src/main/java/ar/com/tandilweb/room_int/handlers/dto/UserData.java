package ar.com.tandilweb.room_int.handlers.dto;

import ar.com.tandilweb.exchange.userAuth.Challenge;
import ar.com.tandilweb.exchange.userAuth.types.ChallengeActions;
import ar.com.tandilweb.persistence.domain.Users;

public class UserData {
	
	public long userID;
	public String sessID;
	public Challenge lastChallenge;
	public String transactionID;
	public ChallengeActions challengeAction;
	public UserDataStatus status;
	
	public long chips; // chips in table
	public Users dataBlock; // user of DB.
	public long requestForDeposit;
}
