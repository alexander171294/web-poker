package ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.InGameSchema;

public class ICardDist extends InGameSchema {
	
	public int position;
	public SchemaCard[] cards;
	
	public ICardDist() {
		super("iCardDist");
	}
}
