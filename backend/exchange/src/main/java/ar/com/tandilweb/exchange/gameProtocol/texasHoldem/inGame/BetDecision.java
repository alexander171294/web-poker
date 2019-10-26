package ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.InGameSchema;

public class BetDecision extends InGameSchema{
	
	public long toCall;
	public boolean canCheck;
	public long minRaise;
	public long maxRaise;
	
	public BetDecision() {
		super("betDecision");
	}

}
