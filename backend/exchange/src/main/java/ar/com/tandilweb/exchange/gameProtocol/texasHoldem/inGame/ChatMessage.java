package ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.InGameSchema;

public class ChatMessage extends InGameSchema{
	
	public String message;
	public String author;

	public ChatMessage() {
		super("ChatMessage");
	}

}
