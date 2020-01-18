package ar.com.tandilweb.exchange.gameProtocol.texasHoldem.accessing;

import java.util.List;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.SchemaCard;

public class SnapshotPlayer {
	
	public String nick;
	public String photo;
	public long chips;
	public long actualBet;
	public boolean haveCards;
	public boolean showingCards;
	public List<SchemaCard> cards;

}
