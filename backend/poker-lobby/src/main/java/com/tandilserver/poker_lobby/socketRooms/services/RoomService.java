package com.tandilserver.poker_lobby.socketRooms.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tandilserver.poker_intercom.handshake.ServerInfo;
import com.tandilserver.poker_lobby.dataBase.domain.Rooms;
import com.tandilserver.poker_lobby.dataBase.repository.RoomsRepository;

@Service
public class RoomService {
	
	@Autowired
	protected RoomsRepository roomsRepository;
	
	public Rooms addIfNotExistsRoomServer(ServerInfo srvInfo) {
		Rooms room = roomsRepository.findByADDR(srvInfo.ip, srvInfo.port);
		if(room == null) {
			room = new Rooms();
			room.setBlind(srvInfo.blind);
			room.setIp(srvInfo.ip);
			room.setLimit_bet(srvInfo.limit_bet.ordinal());
			room.setMin_bet(srvInfo.min_bet);
			room.setPlayers(srvInfo.players);
			room.setPort(srvInfo.port);
			room.setServer_name(srvInfo.server_name);
			room.setServer_type(srvInfo.server_type.ordinal());
			room.setServerIdentityHash(UUID.randomUUID().toString());
			room.setNewItem(true);
			room.setOfficialServer(srvInfo.officialServer);
			roomsRepository.create(room);
			return room;
		} else {
			room.setNewItem(false);
			return room;
		}
	}
	
	public boolean validateOldHash(Long id_room, String identityHash) {
		Rooms room = roomsRepository.findById(id_room);
		if(room != null && room.getServerIdentityHash() != null && room.getServerIdentityHash().equals(identityHash)) {
			return true;
		}
		return false;
	}
	
	public Rooms updateRoomHash(Long id_room, String newIdentityHash) {
		Rooms room = roomsRepository.findById(id_room);
		room.setServerIdentityHash(newIdentityHash);
		roomsRepository.update(room);
		return room;
	}

}
