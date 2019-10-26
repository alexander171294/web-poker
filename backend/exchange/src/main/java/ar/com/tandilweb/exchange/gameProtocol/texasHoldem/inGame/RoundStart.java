package ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.InGameSchema;

public class RoundStart  extends InGameSchema{
	
	public int dealerPosition;
	public long roundNumber;
	
	public RoundStart() {
		super("roundStart");
	}

}
