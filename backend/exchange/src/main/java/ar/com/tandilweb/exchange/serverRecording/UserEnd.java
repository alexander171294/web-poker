package ar.com.tandilweb.exchange.serverRecording;

import ar.com.tandilweb.exchange.ServerRecordingSchema;

public class UserEnd extends ServerRecordingSchema {
	
	public long userID;
	public long refoundCoins;
	
	public UserEnd() {
		super("userEnd");
	}

}
