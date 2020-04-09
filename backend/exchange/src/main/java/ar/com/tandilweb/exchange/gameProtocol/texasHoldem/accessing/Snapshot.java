package ar.com.tandilweb.exchange.gameProtocol.texasHoldem.accessing;

import java.util.List;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.AccessSchema;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.BetDecision;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.SchemaCard;

public class Snapshot extends AccessSchema {
	
	public List<SnapshotPlayer> players;
	public double pot;
	public List<SchemaCard> communityCards;
	public boolean isInRest;
	public boolean isDealing;
	public int dealerPosition = -1;
	public int roundStep;
	public int myPosition = -1;
	public int waitingFor;
	public BetDecision betDecision;

	public Snapshot() {
		super("snapshot");
	}

}
