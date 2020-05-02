package ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.InGameSchema;

public class DepositAnnouncement extends InGameSchema {
	
	public Integer position;
	public Long quantity;
	
	public DepositAnnouncement() {
		super("DepositAnnouncement");
	}

	public DepositAnnouncement(Integer position, Long quantity) {
		super("DepositAnnouncement");
		this.position = position;
		this.quantity = quantity;
	}

}
