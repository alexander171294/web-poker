package ar.com.tandilweb.room_poker.deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.tandilweb.room_poker.deck.cards.Card;
import ar.com.tandilweb.room_poker.deck.cards.CardComparator;

public class Deck {
	
	private static final Logger log = LoggerFactory.getLogger(Deck.class);
	
	private List<Card> deck = new ArrayList<Card>();
	private int index;
	
	public Deck() {
		this.add1Deck();
	}
	
	private void add1Deck() {
		for(int suit = 0; suit < 4; suit++) {
			for(int cardValue = 0; cardValue <= 13; cardValue++) {
				if(cardValue == 1) continue; // the value one isn't exists, the Ace is equals to zero.
				Card card = new Card(suit, cardValue);
				deck.add(card);
			}
		}
		log.debug("New deck created. Deck have "+deck.size()+" cards");
	}
	
	public void shuffle() {
		Collections.shuffle(deck);
		index = 0;
		log.debug("Cards shuffled.");
	}
	
	public Card getNextCard() {
		index++; // increment index.
		return deck.get(index-1); // get the card.
	}
	
	public HandValues getHandData(List<Card> handCards, List<Card> tableCards) {
		HandValues data = new HandValues();
		List<Card> allCardsInGame = new ArrayList<Card>();
		allCardsInGame.addAll(handCards);
		allCardsInGame.addAll(tableCards);
		allCardsInGame.sort(new CardComparator());
		
		int HandPoints;
		int KickerPoint;
		String handName;
		String kickerName;
		
		// search for pairs:
		
		// search for two pair:
		
		// search for a trips (three of a kind)
		
		// search for Straight
		
		// search flush
		
		// search full house
		
		// search for quads (four of a kind)
		
		// sarch for Straight Flush
		
		// Royal Flush
		
		return data;
	}

}
