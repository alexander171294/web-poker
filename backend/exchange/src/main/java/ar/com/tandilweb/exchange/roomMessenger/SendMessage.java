package ar.com.tandilweb.exchange.roomMessenger;

import ar.com.tandilweb.exchange.RoomMessengerSchema;

public class SendMessage extends RoomMessengerSchema {
	
	public String message;
	
	public SendMessage() {
		super("sendMessage");
	}

}
