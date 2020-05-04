package ar.com.tandilweb.ApiServer.dataTypesObjects.users;

import ar.com.tandilweb.ApiServer.dataTypesObjects.generic.GeneralResponse;

public class UserProfile extends GeneralResponse {
	
	public long idUser;
	public String nick;
	public String photo;
	public long chips;
	public String validation_code;
	
}
