package ar.com.tandilweb.exchange.orchestratorLSL;

import java.util.List;

import ar.com.tandilweb.exchange.OrchestratorLSLSchema;

public class Rooms<RoomDataTemplate> extends OrchestratorLSLSchema {
	
	public List<RoomDataTemplate> list;
	
	public Rooms() {
		super("rooms");
	}

}
