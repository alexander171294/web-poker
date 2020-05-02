package ar.com.tandilweb.room_poker.deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.tandilweb.room_poker.deck.cards.Card;
import ar.com.tandilweb.room_poker.deck.cards.CardComparator;
import ar.com.tandilweb.room_poker.deck.cards.Suit;
import ar.com.tandilweb.room_poker.deck.hands.HandType;
import ar.com.tandilweb.room_poker.deck.hands.HandValues;

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
		// TODO: homologate this function.
		HandValues data = new HandValues();
		List<Card> allCardsInGame = new ArrayList<Card>();
		allCardsInGame.addAll(handCards);
		allCardsInGame.addAll(tableCards);
		handCards.sort(new CardComparator());
		// sorter cards:
		allCardsInGame.sort(new CardComparator());
		
		Map<Integer, List<Card>> groupedCards = new HashMap<Integer, List<Card>>();
		List<Integer> pairs = new ArrayList<Integer>();
		List<Integer> trips = new ArrayList<Integer>();
		List<Integer> quads = new ArrayList<Integer>();
		
		int cardOfHearth = 0;
		int cardOfDiamond = 0;
		int cardOfClub = 0;
		int cardOfSpades = 0;
		int bigFlush = 0;
		
		int lastValue = 1;
		int countConsecutives = 0;
		int bigStraight = 0;
		boolean straightFlush = true;
		int suitStraight = -1;
		
		for (Card card: allCardsInGame) {
			// group cards:
			if(!groupedCards.containsKey(card.value.getNumericValueBigACE())) {
				groupedCards.put(card.value.getNumericValueBigACE(), new ArrayList<Card>());
			}
			List<Card> cardsGrouped = groupedCards.get(card.value.getNumericValueBigACE());
			cardsGrouped.add(card);
			if(cardsGrouped.size() == 4) {
				quads.add(card.value.getNumericValueBigACE());
				trips.remove(trips.indexOf(card.value.getNumericValueBigACE()));
			} else if(cardsGrouped.size() == 3) {
				trips.add(card.value.getNumericValueBigACE());
				pairs.remove(pairs.indexOf(card.value.getNumericValueBigACE()));
			} else if(cardsGrouped.size() == 2) {
				pairs.add(card.value.getNumericValueBigACE());
			}
			// Flush?
			if(card.suit.equals(Suit.CLUB)) {
				cardOfClub++;
				if(cardOfClub > 4) {
					bigFlush = card.value.getNumericValueBigACE();
				}
			}
			if(card.suit.equals(Suit.DIAMOND)) {
				cardOfDiamond++;
				if(cardOfDiamond > 4) {
					bigFlush = card.value.getNumericValueBigACE();
				}
			}
			if(card.suit.equals(Suit.HEARTH)) {
				cardOfHearth++;
				if(cardOfHearth > 4) {
					bigFlush = card.value.getNumericValueBigACE();
				}
			}
			if(card.suit.equals(Suit.SPADE)) {
				cardOfSpades++;
				if(cardOfSpades > 4) {
					bigFlush = card.value.getNumericValueBigACE();
				}
			}
			
			// straight 
			// TODO: consider ACE as One.
			if(lastValue == card.value.getNumericValueBigACE() - 1 && countConsecutives < 5)  { //  when count is over 4
				if(suitStraight == -1) {
					suitStraight = card.suit.ordinal();
				}
				countConsecutives++;
				bigStraight = card.value.getNumericValueBigACE();
				if(suitStraight != card.suit.ordinal()) {
					straightFlush = false;
				}
			} else if(countConsecutives < 5 && lastValue == card.value.getNumericValueBigACE()) { // ignore equals or when count is over 4
				countConsecutives = 0;
				suitStraight = -1;
				straightFlush = true;
			}
			lastValue = card.value.getNumericValueBigACE();
			
		}
		
		// search for pairs:
		boolean havePairs = pairs.size() >= 1;
		// search for two pair:
		boolean haveTwoPairs = pairs.size() > 1;
		// search for a trips (three of a kind)
		boolean haveTrips = trips.size() > 0;
		// search for Straight
		boolean haveStraight = countConsecutives >= 5;
		// search flush
		boolean haveFlush = cardOfClub >= 5 || cardOfDiamond >= 5 || cardOfHearth >= 5 || cardOfSpades >= 5;
		// search full house
		boolean haveFullHouse = havePairs && haveTrips;
		// search for quads (four of a kind)
		boolean haveQuads = quads.size() == 1;
		// sarch for Straight Flush && Royal Flush
		boolean haveStraightFlush = haveStraight && straightFlush;
		
		if(haveStraightFlush) {
			// 112 + value of big card in straight
			data.handPoints = 112 + bigStraight;
			data.kickerPoint = 0;
			data.handName = "Straight Flush To " + getNameOf(bigStraight) + " ("+data.handPoints+"/126)";
			data.type = HandType.STRAIGHT_FLUSH;
		} else if(haveQuads) {
			// 98 + value of Quad
			int bigQuad = groupedCards.get(quads.get(0)).get(0).value.getNumericValueBigACE();
			data.handPoints = 98 + bigQuad;
			data.kickerPoint = this.getSecondaryCardValueOf(handCards, bigQuad);
			data.handName = "Quads of " + getNameOf(bigQuad) + "s ("+data.handPoints+"/126)";
			data.kickerName = "Biggest card " + getNameOf(data.kickerPoint);
			data.type = HandType.FOUR_QUADS;
		} else if(haveFullHouse) {
			// 84 + value of trip.
			int bigFull = groupedCards.get(trips.get(trips.size()-1)).get(0).value.getNumericValueBigACE();
			data.handPoints = 84 + bigFull;
			// secondary value of pair
			data.secondaryHandPoint = groupedCards.get(pairs.get(pairs.size()-1)).get(0).value.getNumericValueBigACE();
			data.kickerPoint = 0;
			data.handName = "Full House of " + getNameOf(bigFull) + "s with " + getNameOf(data.secondaryHandPoint) + "s ("+data.handPoints+"/126)";
			data.type = HandType.FULL_HOUSE;
		} else if(haveFlush) {
			// 70 + value of big card in flush
			data.handPoints = 70 + bigFlush;
			data.kickerPoint = 0;
			data.handName = "Flush with big card " + getNameOf(bigFlush) + " ("+data.handPoints+"/126)";
			data.type = HandType.FLUSH;
		} else if(haveStraight) {
			// 56 + value of straight
			data.handPoints = 56 + bigStraight;
			data.kickerPoint = 0;
			data.handName = "Straight to " + getNameOf(bigStraight) + " ("+data.handPoints+"/126)";
			data.type = HandType.STRAIGHT;
		} else if(haveTrips) {
			// 42 + value of trip
			int bigTrip = groupedCards.get(trips.get(trips.size()-1)).get(0).value.getNumericValueBigACE();
			data.handPoints = 42 + bigTrip;
			data.kickerPoint = this.getSecondaryCardValueOf(handCards, bigTrip);
			data.handName = "Trips of " + getNameOf(bigTrip) + " ("+data.handPoints+"/126)";
			data.kickerName = "Biggest card " + getNameOf(data.kickerPoint);
			data.type = HandType.TRIPS;
		} else if(haveTwoPairs) {
			// 28 + value of big pair
			int bigPair = groupedCards.get(pairs.get(pairs.size()-1)).get(0).value.getNumericValueBigACE();
			data.handPoints = 28 + bigPair;
			// secondary value of low pair
			data.secondaryHandPoint = groupedCards.get(pairs.get(pairs.size()-2)).get(0).value.getNumericValueBigACE();
			// kicker
			data.kickerPoint = this.getSecondaryCardValueOf(handCards, bigPair);
			// discard if is the same
			if(data.kickerPoint == data.secondaryHandPoint) {
				data.kickerPoint = this.getSecondaryCardValueOf(handCards, data.secondaryHandPoint);
			}
			// discard if is the same:
			data.kickerPoint = data.kickerPoint == bigPair || data.kickerPoint == data.secondaryHandPoint ? 0 : data.kickerPoint;
			data.handName = "Two Pair of " + getNameOf(bigPair) + " and " + getNameOf(data.secondaryHandPoint) + " ("+data.handPoints+"/126)";
			data.kickerName = "Biggest card " + getNameOf(data.kickerPoint);
			data.type = HandType.TWO_PAIRS;
		} else if(havePairs) {
			// 14 + value of pairs
			int bigCard = groupedCards.get(pairs.get(0)).get(0).value.getNumericValueBigACE();
			data.handPoints = 14 + bigCard;
			data.kickerPoint = this.getSecondaryCardValueOf(handCards, bigCard);
			data.handName = "Pair of " + getNameOf(bigCard) + " ("+data.handPoints+"/126)";
			data.kickerName = "Biggest card " + getNameOf(data.kickerPoint);
			data.type = HandType.PAIRS;
		} else {
			// 2-14
			data.handPoints = handCards.get(1).value.getNumericValueBigACE();
			data.kickerPoint = handCards.get(0).value.getNumericValueBigACE(); 
			data.handName = "Pair of " + getNameOf(data.handPoints) + " ("+data.handPoints+"/126)";
			data.kickerName = "Biggest card " + getNameOf(data.kickerPoint);
			data.type = HandType.BIG_CARD;
		}
		return data;
	}
	
	private int getSecondaryCardValueOf(List<Card> handCards, int bigCard) {
		if(handCards.get(0).value.getNumericValueBigACE() == bigCard) {
			return handCards.get(1).value.getNumericValueBigACE();
		} else if(handCards.get(1).value.getNumericValueBigACE() == bigCard) {
			return handCards.get(0).value.getNumericValueBigACE();
		} else {
			return handCards.get(1).value.getNumericValueBigACE() > bigCard ? handCards.get(1).value.getNumericValueBigACE() : handCards.get(0).value.getNumericValueBigACE();
		}
	}
	
	private String getNameOf(int number) {
		if(number < 11) {
			return ""+number;
		} else if(number == 11) {
			return "Jack";
		} else if(number == 12) {
			return "Queen";
		} else if(number == 13) {
			return "King";
		}
		return "ACE";
	}

}
