package ar.com.tandilweb.room.handlers;

import org.springframework.stereotype.Service;

@Service
public class RoomHandler {
	
	private long roomID;

	public long getRoomID() {
		return roomID;
	}

	public void setRoomID(long roomID) {
		this.roomID = roomID;
	}

}
