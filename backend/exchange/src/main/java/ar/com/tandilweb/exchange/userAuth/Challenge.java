package ar.com.tandilweb.exchange.userAuth;

import ar.com.tandilweb.exchange.UserAuthSchema;
import ar.com.tandilweb.exchange.userAuth.types.ChallengeActions;

public class Challenge extends UserAuthSchema {
	
	public String claimToken;
	public long roomID;
	public ChallengeActions action;
	
	public Challenge() {
		super("challenge");
	}

}
