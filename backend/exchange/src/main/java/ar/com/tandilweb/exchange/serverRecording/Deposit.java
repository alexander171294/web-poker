package ar.com.tandilweb.exchange.serverRecording;

import ar.com.tandilweb.exchange.ServerRecordingSchema;

public class Deposit extends ServerRecordingSchema {
	
	public long userID;
	public long challengeID;
	public String claimToken;
	public long coins;
	
	public Deposit() {
		super("deposit");
	}

}
