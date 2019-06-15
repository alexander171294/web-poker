package ar.com.tandilweb.exchange.roomOperations;

import ar.com.tandilweb.exchange.RoomOperationsSchema;

public class BanUser extends RoomOperationsSchema {
	
	public long targetID;
	public String messageReason;
	
	public BanUser() {
		super("banUser");
	}

}
