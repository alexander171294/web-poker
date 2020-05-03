package ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame;

import java.util.ArrayList;
import java.util.List;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.InGameSchema;

public class Pots extends InGameSchema {
	public List<Long> pots = new ArrayList<Long>();
	public Pots() {
		super("Pots");
	}

}
