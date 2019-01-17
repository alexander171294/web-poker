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
			roomsRepository.create(room);
			return room;
		}
		return null;
	}

}
