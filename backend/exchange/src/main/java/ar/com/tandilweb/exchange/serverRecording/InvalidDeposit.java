package ar.com.tandilweb.exchange.serverRecording;

import ar.com.tandilweb.exchange.ServerRecordingSchema;

public class InvalidDeposit extends ServerRecordingSchema {
	
	public long userID;
	
	public InvalidDeposit() {
		super("invalidDeposit");
	}

}
