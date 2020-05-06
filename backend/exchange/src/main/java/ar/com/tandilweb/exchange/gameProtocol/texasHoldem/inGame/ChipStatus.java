package ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame;

import java.util.List;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.InGameSchema;

public class ChipStatus extends InGameSchema {
	
	public List<IndividualChipStatus> status;

	public ChipStatus() {
		super("ChipStatus");
	}

}
