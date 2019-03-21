package ar.com.tandilweb.exchange.orchestratorLSL;

import ar.com.tandilweb.exchange.OrchestratorLSLSchema;

public class FriendshipRequest extends OrchestratorLSLSchema {
	
	public long idTarget;
	
	public FriendshipRequest() {
		super("friendshipRequest");
	}

}
