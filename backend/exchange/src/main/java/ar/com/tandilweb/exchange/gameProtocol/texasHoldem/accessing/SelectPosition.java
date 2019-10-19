package ar.com.tandilweb.exchange.gameProtocol.texasHoldem.accessing;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.AccessSchema;

public class SelectPosition extends AccessSchema {

	public int position;
	
	public SelectPosition() {
		super("selectPosition");
	}
	
}
