package ar.com.tandilweb.exchange.roomAuth;

import ar.com.tandilweb.exchange.RoomAuthSchema;

public class TokenUpdate extends RoomAuthSchema {
	
	public String securityToken;
	
	public TokenUpdate() {
		super("tokenUpdate");
	}

}
