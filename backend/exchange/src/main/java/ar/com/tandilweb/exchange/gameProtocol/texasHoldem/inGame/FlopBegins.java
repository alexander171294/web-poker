package ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.InGameSchema;

public class FlopBegins extends InGameSchema {
	public SchemaCard[] cards;
	public FlopBegins() {
		super("flopBegins");
	}
}
