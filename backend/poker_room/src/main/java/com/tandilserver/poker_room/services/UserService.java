package com.tandilserver.poker_room.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.tandilserver.poker_room.services.dto.UserData;

@Service
public class UserService {
	
	public Map<String, UserData> usersDataBlocks = new HashMap<String, UserData>();
	public Map<String, String> socketUserAssociation = new HashMap<String, String>();

	public String registerViewer(UserData viewer) {
		String uuid = UUID.randomUUID().toString();
		socketUserAssociation.put(viewer.socketSessionUUID, uuid);
		usersDataBlocks.put(uuid, viewer);
		return uuid;
	}
	
	public void markAsValid(String uuid, Long coins) {
		UserData ud = usersDataBlocks.get(uuid);
		ud.coins_registered = coins;
		ud.verified = true;
	}
	
	public String getMessageRouterUUID(String userID) {
		return usersDataBlocks.get(userID).messageRouterSessionUUID;
	}
	
	

}
