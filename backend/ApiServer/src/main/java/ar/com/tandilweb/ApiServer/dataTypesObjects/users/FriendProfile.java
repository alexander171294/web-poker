package ar.com.tandilweb.ApiServer.dataTypesObjects.users;

import java.util.List;

import ar.com.tandilweb.persistence.domain.UsersInRooms;

public class FriendProfile {
	public long idUser;
	public String nick;
	public String photo;
	public List<UsersInRooms> rooms;
	public Boolean inGame;
}
