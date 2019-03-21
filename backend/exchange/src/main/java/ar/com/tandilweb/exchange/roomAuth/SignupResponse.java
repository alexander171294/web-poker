package ar.com.tandilweb.exchange.roomAuth;

import ar.com.tandilweb.exchange.RoomAuthSchema;

public class SignupResponse extends RoomAuthSchema {
	
	public long serverID;
	public String securityToken;
	
	public SignupResponse() {
		super("signupResponse");
	}

}
