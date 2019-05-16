package ar.com.tandilweb.ApiServer.transport;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.tandilweb.ApiServer.dataTypesObjects.lobby.ChallengeResponse;
import ar.com.tandilweb.ApiServer.dataTypesObjects.lobby.RoomResponse;
import ar.com.tandilweb.ApiServer.persistence.repository.RoomsRepository;

@Service
public class LobbyAdapter {
	
	@Autowired
	RoomsRepository roomsRepository;
	
	public List<RoomResponse> getRooms() {
		roomsRepository.getAll();
		return null;
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
