package ar.com.tandilweb.ApiServer.transport;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.tandilweb.ApiServer.dataTypesObjects.friends.FriendRequest;
import ar.com.tandilweb.ApiServer.dataTypesObjects.generic.ValidationException;
import ar.com.tandilweb.ApiServer.dataTypesObjects.users.UserProfile;
import ar.com.tandilweb.ApiServer.persistence.domain.Users;
import ar.com.tandilweb.ApiServer.persistence.repository.FriendshipsRepository;
import ar.com.tandilweb.ApiServer.persistence.repository.UsersRepository;

@Service
public class FriendsAdapter {
	
	@Autowired
	FriendshipsRepository friendshipsRepository;
	
	@Autowired
	UsersRepository usersRepository;
	
	public List<UserProfile> getFriends(long me) throws ValidationException {
		//List<Friendships> fs = friendshipsRepository.getAllFor(me);
		List<Users> users = usersRepository.getFromFriendshipsFor(me);
		List<UserProfile> out = new ArrayList<UserProfile>();
		for(Users user: users) {
			UserProfile uP = new UserProfile();
			uP.idUser = user.getId_user();
			uP.nick = user.getNick_name();
			uP.photo = user.getPhoto();
			out.add(uP);
		}
		return out;
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
