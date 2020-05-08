package ar.com.tandilweb.exchange.gameProtocol.texasHoldem.accessing;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.AccessSchema;

public class Announcement extends AccessSchema {
	
	public int position;
	public String user;
	public String avatar;
	public Long chips;
	public Long userID;

	public Announcement() {
		super("announcement");
	}

}
