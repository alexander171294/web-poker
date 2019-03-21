package ar.com.tandilweb.exchange.roomOperations;

import ar.com.tandilweb.exchange.RoomOperationsSchema;

public class Deny extends RoomOperationsSchema {
	
	public String messageReason;
	
	public Deny() {
		super("deny");
	}

}
