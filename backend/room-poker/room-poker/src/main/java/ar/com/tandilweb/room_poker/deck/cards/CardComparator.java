package ar.com.tandilweb.room_poker.deck.cards;

import java.util.Comparator;

public class CardComparator implements Comparator<Card> {
	
	public int compare(Card a, Card b) {
		return a.value.getNumericValue() - b.value.getNumericValue();
	}
	
}
