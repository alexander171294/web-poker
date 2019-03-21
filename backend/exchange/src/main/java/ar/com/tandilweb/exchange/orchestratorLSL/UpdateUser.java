package ar.com.tandilweb.exchange.orchestratorLSL;

import ar.com.tandilweb.exchange.OrchestratorLSLSchema;

public class UpdateUser<UserDataTemplate> extends OrchestratorLSLSchema {
	
	public long userID;
	public UserDataTemplate data;
	
	public UpdateUser() {
		super("updateUser");
	}

}
