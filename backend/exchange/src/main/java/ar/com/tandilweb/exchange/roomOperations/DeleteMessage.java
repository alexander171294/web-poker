package ar.com.tandilweb.exchange.roomOperations;

import ar.com.tandilweb.exchange.RoomOperationsSchema;

public class DeleteMessage extends RoomOperationsSchema {
	
	public long messageID;
	
	public DeleteMessage() {
		super("deleteMessage");
	}

}
