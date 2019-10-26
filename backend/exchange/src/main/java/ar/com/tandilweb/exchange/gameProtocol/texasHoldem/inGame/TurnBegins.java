package ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.InGameSchema;

public class TurnBegins extends InGameSchema{

	public SchemaCard card;
	
	public TurnBegins() {
		super("turnBegins");
	}
}
