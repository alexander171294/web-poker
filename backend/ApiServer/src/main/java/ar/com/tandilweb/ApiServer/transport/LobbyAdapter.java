package ar.com.tandilweb.ApiServer.transport;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.tandilweb.ApiServer.dataTypesObjects.lobby.ChallengeResponse;
import ar.com.tandilweb.ApiServer.dataTypesObjects.lobby.RoomResponse;
import ar.com.tandilweb.ApiServer.persistence.domain.Rooms;
import ar.com.tandilweb.ApiServer.persistence.repository.RoomsRepository;

@Service
public class LobbyAdapter {
	
	@Autowired
	RoomsRepository roomsRepository;
	
	public List<RoomResponse> getRooms() {
		List<RoomResponse> out = new ArrayList<RoomResponse>();
		List<Rooms> rooms = roomsRepository.getConnecteds();
		for(Rooms room: rooms) {
			RoomResponse item = new RoomResponse();
			item.description = room.getDescription();
			item.gproto = room.getGproto();
			item.id_room = room.getId_room();
			item.isOfficial = room.isOfficial();
			item.max_players = room.getMax_players();
			item.minCoinForAccess = room.getMinCoinForAccess();
			item.name = room.getName();
			item.server_ip = room.getServer_ip();
			out.add(item);
		}
		return out;
	}
	
	public List<RoomResponse> getRoomsByProtocol(String gproto) {
		return null;
	}
	
	public RoomResponse getRoomByID(long roomID) {
		return null;
	}
	
	public ChallengeResponse challengeRoomByID(long roomID)  {
		return null;
	}

}
