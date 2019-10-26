package ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.InGameSchema;

public class CardDist extends InGameSchema {
	
	public int position;
	public boolean[] cards;
	
	public CardDist() {
		super("cardDist");
	}
}
