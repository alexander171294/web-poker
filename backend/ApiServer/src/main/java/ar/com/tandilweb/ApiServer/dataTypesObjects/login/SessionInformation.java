package ar.com.tandilweb.ApiServer.dataTypesObjects.login;

import ar.com.tandilweb.ApiServer.dataTypesObjects.generic.GeneralResponse;

public class SessionInformation extends GeneralResponse {
	
	public long userID;
	public String jwtToken;
	public long sessionID;

}
