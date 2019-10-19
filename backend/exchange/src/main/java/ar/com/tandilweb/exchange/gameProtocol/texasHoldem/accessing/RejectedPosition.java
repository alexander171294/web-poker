package ar.com.tandilweb.exchange.gameProtocol.texasHoldem.accessing;

import java.util.List;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.AccessSchema;

public class RejectedPosition extends AccessSchema {
	
	public List<Integer> positions;

	public RejectedPosition() {
		super("rejectedPosition");
	}

}
