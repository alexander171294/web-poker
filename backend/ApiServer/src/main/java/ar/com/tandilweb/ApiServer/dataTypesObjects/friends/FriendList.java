package ar.com.tandilweb.ApiServer.dataTypesObjects.friends;

import java.util.List;

import ar.com.tandilweb.ApiServer.dataTypesObjects.generic.GeneralResponse;
import ar.com.tandilweb.ApiServer.dataTypesObjects.users.FriendProfile;

public class FriendList extends GeneralResponse{
	public List<FriendProfile> friends;
}
