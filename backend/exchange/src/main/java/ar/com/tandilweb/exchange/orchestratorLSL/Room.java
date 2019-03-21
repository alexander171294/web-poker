package ar.com.tandilweb.exchange.orchestratorLSL;

import ar.com.tandilweb.exchange.OrchestratorLSLSchema;

public class Room<RoomDataTemplate> extends OrchestratorLSLSchema {
	
	public RoomDataTemplate data;
	
	public Room() {
		super("room");
	}

}
