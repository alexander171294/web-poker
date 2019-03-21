package ar.com.tandilweb.exchange.serverRecording;

import ar.com.tandilweb.exchange.ServerRecordingSchema;

public class SuccessDeposit extends ServerRecordingSchema {
	
	public long userID;
	
	public SuccessDeposit() {
		super("successDeposit");
	}

}
