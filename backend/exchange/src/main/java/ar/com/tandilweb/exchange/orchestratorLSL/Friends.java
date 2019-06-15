package ar.com.tandilweb.exchange.orchestratorLSL;

import java.util.List;

import ar.com.tandilweb.exchange.OrchestratorLSLSchema;

public class Friends<UserDataTemplate> extends OrchestratorLSLSchema {
	
	public List<UserDataTemplate> list;
	
	public Friends() {
		super("friends");
	}

}
