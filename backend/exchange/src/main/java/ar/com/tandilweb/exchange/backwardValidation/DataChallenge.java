package ar.com.tandilweb.exchange.backwardValidation;

import ar.com.tandilweb.exchange.BackwardValidationSchema;

public class DataChallenge<UserDataTemplate> extends BackwardValidationSchema {
	
	public long idUser;
	public String claimToken;
	public UserDataTemplate userData;
	
	public DataChallenge() {
		super("dataChallenge");
	}

}
