package ar.com.tandilweb.ApiServer.transport;

import org.springframework.stereotype.Service;

import ar.com.tandilweb.ApiServer.dataTypesObjects.users.UserProfile;
import ar.com.tandilweb.ApiServer.dataTypesObjects.users.UserProfileEdited;

@Service
public class UsersAdapter {
	
	public UserProfile getUserByID(long id) {
		return null;
	}
	
	public UserProfile updateProfileByID(long id, UserProfileEdited userProfile) {
		return null;
	}

}
