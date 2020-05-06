package ar.com.tandilweb.ApiServer.transport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.tandilweb.ApiServer.dataTypesObjects.generic.ValidationException;
import ar.com.tandilweb.ApiServer.dataTypesObjects.users.FriendProfile;
import ar.com.tandilweb.ApiServer.dataTypesObjects.users.UserProfile;
import ar.com.tandilweb.ApiServer.persistence.repository.FriendshipsRepository;
import ar.com.tandilweb.ApiServer.persistence.repository.UsersInRoomsRepository;
import ar.com.tandilweb.ApiServer.persistence.repository.UsersRepository;
import ar.com.tandilweb.persistence.domain.Friendships;
import ar.com.tandilweb.persistence.domain.Users;
import ar.com.tandilweb.persistence.domain.UsersInRooms;

@Service
public class FriendsAdapter {

	@Autowired
	FriendshipsRepository friendshipsRepository;

	@Autowired
	UsersRepository usersRepository;
	
	@Autowired
	UsersInRoomsRepository usersInRoomsRepository;

	public List<FriendProfile> getFriends(long me) throws ValidationException {
		// List<Friendships> fs = friendshipsRepository.getAllFor(me);
		List<Users> users = usersRepository.getFromFriendshipsFor(me);
		List<FriendProfile> out = new ArrayList<FriendProfile>();
		for (Users user : users) {
			FriendProfile uP = new FriendProfile();
			uP.idUser = user.getId_user();
			uP.nick = user.getNick_name();
			uP.photo = user.getPhoto();
			uP.rooms = getRooms(user.getId_user());
			uP.inGame = uP.rooms != null ? true : false;
			out.add(uP);
		}
		return out;
	}
	
	private List<UsersInRooms> getRooms(long id) {
		var roomlist = usersInRoomsRepository.findPlayerRooms(id);
		if (roomlist != null) {
			return roomlist;
		} else return null;
	}

	public boolean deleteFriend(long me, long friendID) throws ValidationException {
		Friendships fs = friendshipsRepository.findFor(me, friendID);
		if (fs == null) {
			throw new ValidationException(2, "Friendship doesn't exists");
		}
		friendshipsRepository.delete(fs);
		return true;
	}

	public boolean acceptRequest(long me, long requesterID) throws ValidationException {
		Friendships fs = friendshipsRepository.findByOriginTarget(requesterID, me);
		if (fs == null) {
			throw new ValidationException(2, "Friendship doesn't exists");
		}
		if (fs.isAccepted()) {
			return false;
		} else {
			fs.setAccepted(true);
			friendshipsRepository.update(fs);
			return true;
		}
	}

	public boolean sendRequest(long me, long userID) throws ValidationException {
		Friendships fs = friendshipsRepository.findFor(userID, me);
		Users user = usersRepository.findById(userID);
		if(user == null) {
			throw new ValidationException(2, "User target doesn't exists.");
		}
		if (fs != null) {
			throw new ValidationException(3, "Friendship already exists.");
		}
		fs = new Friendships();
		fs.setId_user_origin(me);
		fs.setId_user_target(userID);
		fs.setAccepted(false);
		fs.setRequested(new Date());
		friendshipsRepository.create(fs);
		return true;

	}

	public List<UserProfile> getFriendRequests(long me) throws ValidationException {
		List<Users> users = usersRepository.getFromFriendshipsPendingsFor(me);
		List<UserProfile> out = new ArrayList<UserProfile>();
		for (Users user : users) {
			UserProfile uP = new UserProfile();
			uP.idUser = user.getId_user();
			uP.nick = user.getNick_name();
			uP.photo = user.getPhoto();
			out.add(uP);
		}
		return out;
	}

}
