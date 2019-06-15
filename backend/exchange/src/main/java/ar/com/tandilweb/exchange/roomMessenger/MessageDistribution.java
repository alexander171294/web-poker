package ar.com.tandilweb.exchange.roomMessenger;

import java.util.Date;

import ar.com.tandilweb.exchange.RoomMessengerSchema;

public class MessageDistribution extends RoomMessengerSchema {
	
	public long messageID;
	public String message;
	public long userID;
	public String userName;
	public Date date;
	
	public MessageDistribution() {
		super("messageDistribution");
	}

}
