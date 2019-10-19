package ar.com.tandilweb.exchange.gameProtocol.texasHoldem.accessing;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.AccessSchema;

public class Ingress extends AccessSchema {
	
	public Long chips;
	public int position;

	public Ingress() {
		super("ingress");
	}

}
