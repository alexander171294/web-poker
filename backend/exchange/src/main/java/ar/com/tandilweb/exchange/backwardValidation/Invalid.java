package ar.com.tandilweb.exchange.backwardValidation;

import ar.com.tandilweb.exchange.BackwardValidationSchema;

public class Invalid extends BackwardValidationSchema {
	
	public String transactionID;
	
	public Invalid() {
		super("invalid");
	}

}
