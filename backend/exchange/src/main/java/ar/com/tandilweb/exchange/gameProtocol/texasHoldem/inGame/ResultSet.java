package ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame;

import java.util.List;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.InGameSchema;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.generic.Winner;

public class ResultSet extends InGameSchema {
	
	public List<Winner> winners;

	public ResultSet() {
		super("resultSet");
	}

}
