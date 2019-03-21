package ar.com.tandilweb.exchange.serverRecording;

import ar.com.tandilweb.exchange.ServerRecordingSchema;

public class UserBanned extends ServerRecordingSchema {
	
	public long userID;
	
	public UserBanned() {
		super("userBanned");
	}

}
