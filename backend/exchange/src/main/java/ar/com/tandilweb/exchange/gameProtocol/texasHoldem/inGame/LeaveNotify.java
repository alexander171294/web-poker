package ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.InGameSchema;

public class LeaveNotify extends InGameSchema{

	public int position;
	
	public LeaveNotify() {
		super("leaveNotify");
	}
	
}
