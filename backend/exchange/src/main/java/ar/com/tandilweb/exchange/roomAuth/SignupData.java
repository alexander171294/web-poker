package ar.com.tandilweb.exchange.roomAuth;

import ar.com.tandilweb.exchange.RoomAuthSchema;

public class SignupData extends RoomAuthSchema {
	
	public String recoveryEmail;
	
	public SignupData() {
		super("signupData");
	}

}
