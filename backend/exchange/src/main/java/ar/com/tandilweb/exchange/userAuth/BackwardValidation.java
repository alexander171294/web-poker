package ar.com.tandilweb.exchange.userAuth;

import ar.com.tandilweb.exchange.UserAuthSchema;
import ar.com.tandilweb.exchange.userAuth.types.ChallengeActions;

public class BackwardValidation extends UserAuthSchema {
	
	public long idChallenge;
	public ChallengeActions action;
	
	public BackwardValidation() {
		super("backwardValidation");
	}

}
