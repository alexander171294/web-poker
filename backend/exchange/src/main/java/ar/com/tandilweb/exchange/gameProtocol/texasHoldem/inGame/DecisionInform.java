package ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.InGameSchema;

public class DecisionInform extends InGameSchema {
	
	public String action;
	public Integer ammount;
	
	public DecisionInform() {
		super("decisionInform");
	}
}
