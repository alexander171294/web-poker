package ar.com.tandilweb.ApiServer.transport;

import java.util.List;

import org.springframework.stereotype.Service;

import ar.com.tandilweb.ApiServer.dataTypesObjects.friends.FriendRequest;
import ar.com.tandilweb.ApiServer.dataTypesObjects.generic.ValidationException;
import ar.com.tandilweb.ApiServer.dataTypesObjects.users.UserProfile;

@Service
public class FriendsAdapter {
	
	public List<UserProfile> getFriends(long me) throws ValidationException {
		return null;
	}
	
	public boolean deleteFriend(long me, long friendID) {
		return false;
	}
	
	public boolean deleteRequest(long me, long requestID) {
		return false;
	}
	
	public boolean acceptRequest(long me, long requestID) {
		return false;
	}
	
	public boolean sendRequest(long me, long userID) {
		return false;
	}
	
	public List<FriendRequest> getFriendRequests(long me) throws ValidationException {
		return null;
	}
	
}
