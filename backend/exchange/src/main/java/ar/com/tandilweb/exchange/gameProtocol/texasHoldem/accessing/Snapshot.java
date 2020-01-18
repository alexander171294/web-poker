package ar.com.tandilweb.exchange.gameProtocol.texasHoldem.accessing;

import java.util.List;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.AccessSchema;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.SchemaCard;

public class Snapshot extends AccessSchema {
	
	public List<SnapshotPlayer> players;
	public double pot;
	public List<SchemaCard> communityCards;
	public boolean isInRest;
	public boolean isDealing;
	public int dealerPosition;

	public Snapshot() {
		super("snapshot");
	}

}
