package ar.com.tandilweb.exchange.orchestratorLSL;

import ar.com.tandilweb.exchange.OrchestratorLSLSchema;

public class UserDetail<UserDataTemplate> extends OrchestratorLSLSchema {
	
	public long userID;
	public UserDataTemplate data;
	
	public UserDetail() {
		super("userDetail");
	}

}
