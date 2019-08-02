package ar.com.tandilweb.exchange.backwardValidation;

import ar.com.tandilweb.exchange.BackwardValidationSchema;

public class DataChallenge<UserDataTemplate> extends BackwardValidationSchema {
	
	public long idUser;
	public String claimToken;
	public UserDataTemplate userData;
	public String transactionID;
	
	public DataChallenge() {
		super("dataChallenge");
	}

}
