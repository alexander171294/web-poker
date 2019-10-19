package ar.com.tandilweb.exchange.clientOperations;

import ar.com.tandilweb.exchange.ClientOperationsSchema;

public class Deposit extends ClientOperationsSchema {
	
	public long chips;
	
	public Deposit() {
		super("deposit");
	}

}
