package ar.com.tandilweb.exchange.roomMessenger;

import ar.com.tandilweb.exchange.RoomMessengerSchema;

public class DeleteMessageDistribution extends RoomMessengerSchema {
	
	public long messageID;
	
	public DeleteMessageDistribution() {
		super("deleteMessageDistribution");
	}

}
