package ar.com.tandilweb.exchange.roomOperations;

import ar.com.tandilweb.exchange.RoomOperationsSchema;

public class KickUser extends RoomOperationsSchema {
	
	public long targetID;
	public String messageReason;
	
	public KickUser() {
		super("kickUser");
	}

}
