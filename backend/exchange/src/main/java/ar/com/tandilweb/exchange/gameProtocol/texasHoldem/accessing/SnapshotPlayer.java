package ar.com.tandilweb.exchange.gameProtocol.texasHoldem.accessing;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.SchemaCard;

public class SnapshotPlayer {
	
	public String nick;
	public String photo;
	public long chips;
	public long actualBet;
	public boolean showingCards;
	public boolean haveCards;
	public boolean inGame;
	public SchemaCard[] cards;

}
