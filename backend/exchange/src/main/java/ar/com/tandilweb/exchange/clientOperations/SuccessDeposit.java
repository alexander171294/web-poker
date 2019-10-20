package ar.com.tandilweb.exchange.clientOperations;

import ar.com.tandilweb.exchange.ClientOperationsSchema;

public class SuccessDeposit extends ClientOperationsSchema {
	
	public long chips;
	
	public SuccessDeposit() {
		super("successDeposit");
	}

}
