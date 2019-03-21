package ar.com.tandilweb.exchange.orchestratorLSL;

import ar.com.tandilweb.exchange.OrchestratorLSLSchema;

public class RoomChallenge extends OrchestratorLSLSchema {
	
	public long serverID;
	public String claimToken;
	
	public RoomChallenge() {
		super("roomChallenge");
	}

}
