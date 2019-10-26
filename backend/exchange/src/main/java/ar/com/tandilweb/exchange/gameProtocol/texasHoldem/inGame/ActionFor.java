package ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.InGameSchema;

public class ActionFor extends InGameSchema {
	
	public int position;
	public int remainingTime;

	public ActionFor() {
		super("actionFor");
	}
}
