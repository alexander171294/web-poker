package ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.InGameSchema;

public class Blind extends InGameSchema{
	
	public long bbChips;
	public long sbChips;
	public int bbPosition;
	public int sbPosition;

	public Blind() {
		super("blind");
	}

}
