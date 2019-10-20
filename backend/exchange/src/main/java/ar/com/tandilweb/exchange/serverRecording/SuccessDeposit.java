package ar.com.tandilweb.exchange.serverRecording;

import ar.com.tandilweb.exchange.ServerRecordingSchema;

public class SuccessDeposit extends ServerRecordingSchema {
	
	public long depositedChips;
	public long userID;
	public long chips;
	
	public SuccessDeposit() {
		super("successDeposit");
	}

}
