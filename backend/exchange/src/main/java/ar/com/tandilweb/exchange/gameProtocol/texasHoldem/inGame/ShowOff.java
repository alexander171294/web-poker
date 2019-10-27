package ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame;

import java.util.ArrayList;
import java.util.List;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.InGameSchema;

public class ShowOff extends InGameSchema {
	
	public class PositionCard {
		public SchemaCard first;
		public SchemaCard second;
	}
	
	public PositionCard[] positionCards;
	
	public ShowOff(int quantity) {
		super("showOff");
		positionCards = new PositionCard[quantity];
	}
	
	public void setCards(int position, SchemaCard first, SchemaCard second) {
		PositionCard pc = new PositionCard();
		pc.first = first;
		pc.second = second;
		positionCards[position] = pc;
	}
	
}
