package ar.com.tandilweb.exchange.userAuth;

import ar.com.tandilweb.exchange.UserAuthSchema;

public class Authorization extends UserAuthSchema {
	
	public long userID;

	public Authorization() {
		super("authorization");
	}
	
}
