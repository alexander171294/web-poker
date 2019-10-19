package ar.com.tandilweb.exchange.gameProtocol.texasHoldem.accessing;

import java.util.List;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.AccessSchema;

public class DefinePosition extends AccessSchema {
	
	public List<Integer> positions;

	public DefinePosition() {
		super("definePosition");
	}
	
}
