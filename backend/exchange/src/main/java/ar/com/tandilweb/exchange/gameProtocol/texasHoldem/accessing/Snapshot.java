package ar.com.tandilweb.exchange.gameProtocol.texasHoldem.accessing;

import java.util.List;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.AccessSchema;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.BetDecision;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.SchemaCard;

public class Snapshot extends AccessSchema {
	
	public List<SnapshotPlayer> players;
	public List<Long> pots;
	public List<SchemaCard> communityCards;
	public boolean isInRest;
	public boolean isDealing;
	public int dealerPosition = -1;
	public int roundStep;
	public int myPosition = -1;
	public int waitingFor;
	public int smallBlind;
	public int bigBlind;
	public BetDecision betDecision;

	public Snapshot() {
		super("snapshot");
	}

}
