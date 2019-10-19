package ar.com.tandilweb.room.handlers.dto;

import ar.com.tandilweb.exchange.userAuth.Challenge;

public class UserData {
	
	public long userID;
	public String sessID;
	public Challenge lastChallenge;
	public String transactionID;
	public UserDataStatus status;
	
	public long chips;
	
}
