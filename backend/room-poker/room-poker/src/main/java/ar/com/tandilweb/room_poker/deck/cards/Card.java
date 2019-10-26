package ar.com.tandilweb.room_poker.deck.cards;

public class Card {
	
	public Suit suit;
	public CardValue value;
	
	public Card(Suit suit, CardValue cardValue) {
		this.suit = suit;
		this.value = cardValue;
	}
	
	public Card(int suit, int value) {
		if(suit < Suit.values().length && suit >= 0) {
			this.suit = Suit.values()[value];
		}
		if(value < CardValue.values().length && value >= 0) {
			this.value = CardValue.values()[value];
		}
	}

}
