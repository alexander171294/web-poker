package ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.InGameSchema;

public class SnapshotRequest extends InGameSchema {
	
	public int round;

	public SnapshotRequest() {
		super("SnapshotRequest");
	}

}
