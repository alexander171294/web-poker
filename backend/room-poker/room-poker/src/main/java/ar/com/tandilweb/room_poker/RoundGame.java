package ar.com.tandilweb.room_poker;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.ActionFor;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.BetDecision;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.Blind;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.CardDist;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.ICardDist;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.RoundStart;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.SchemaCards;
import ar.com.tandilweb.room_int.handlers.SessionHandlerInt;
import ar.com.tandilweb.room_int.handlers.dto.UserData;
import ar.com.tandilweb.room_poker.deck.Deck;
import ar.com.tandilweb.room_poker.deck.cards.Card;

public class RoundGame {
	
	private static final Logger log = LoggerFactory.getLogger(RoundGame.class);
	
	private static SessionHandlerInt sessionHandler;
	private static long rounds = 0;
	
	// player datas:
	private UserData[] usersInGame;
	private long[] bets;
	private Card[] playerFirstCards;
	private Card[] playerSecondCards;
	
	private int dealerPosition;
	private int tableSize;
	private int waitingActionFromPlayer; // id of player are waiting.
	private int lastActionedPosition; // for cut actions.
	private Deck deck;
	
	public RoundGame(Deck deck, UserData[] usersInGame, int dealerPosition) {
		this.usersInGame = usersInGame;
		this.bets = new long[usersInGame.length];
		this.deck = deck;
		deck.shuffle();
		this.dealerPosition = dealerPosition;
		rounds++;
	}
	
	public void start() {
		RoundStart roundStartSchema = new RoundStart();
		roundStartSchema.dealerPosition = this.dealerPosition;
		roundStartSchema.roundNumber = rounds;
		sessionHandler.sendToAll("/GameController/roundStart", roundStartSchema);
		requestBlind(25, 50); // FXIME: adjust according to configuration.
		try {
			dealCards();
			sendWaitAction(50, false);
		} catch (InterruptedException e) {
			log.warn("Interrupted Exception ", e);
			// FIXME: if this explode, then the cards are never ends to dealing.
		}
	}
	
	private void requestBlind(int smallBlindSize, int bigBlindSize) {
		int smallBlind = Utils.getNextPositionOfPlayers(usersInGame, this.dealerPosition);
		int bigBlind = Utils.getNextPositionOfPlayers(usersInGame, smallBlind);
		Blind blindObject = new Blind();
		blindObject.sbPosition = smallBlind;
		blindObject.sbChips = smallBlindSize;
		blindObject.bbPosition = bigBlind;
		blindObject.bbChips = bigBlindSize;
		usersInGame[smallBlind].chips -= smallBlindSize;
		usersInGame[bigBlind].chips -= bigBlindSize;
		bets[smallBlind] = smallBlindSize;
		bets[bigBlind] = bigBlindSize;
		sessionHandler.sendToAll("/GameController/blind", blindObject);
		waitingActionFromPlayer = Utils.getNextPositionOfPlayers(usersInGame, bigBlind);
		lastActionedPosition = bigBlind;
	}
	
	private void sendWaitAction(long toCall, boolean canCheck) {
		ActionFor aFor = new ActionFor();
		aFor.position = waitingActionFromPlayer;
		aFor.remainingTime = 30; // TODO: adjust according to configuration.
		sessionHandler.sendToAll("/GameController/actionFor", aFor);
		// action for:
		BetDecision bd = new BetDecision();
		bd.toCall = toCall;
		bd.canCheck = canCheck;
		bd.minRaise = toCall; // TODO: adjust according to configuration.
		bd.maxRaise = -1; // TODO: adjust according to configuration.
		// send wait for bet decision:
		sessionHandler.sendToSessID("GameController/betDecision", usersInGame[aFor.position].sessID, bd);
		// TODO: implement timer for wait stop.
	}
	
	private void dealCards() throws InterruptedException {
		List<Integer> players = Utils.getPlayersFromPosition(usersInGame, this.dealerPosition);
		//deck.getNextCard(); // burn a card ?.
		// first iteration:
		for(int position: players) {
			playerFirstCards[position] = deck.getNextCard();
			CardDist cd = new CardDist();
			cd.position = position;
			cd.cards = new boolean[]{ true, false };
			sessionHandler.sendToAll("/GameController/cardsDist", cd); // to all
			ICardDist icd = new ICardDist();
			icd.position = position;
			SchemaCards stCard = new SchemaCards(playerFirstCards[position].suit.ordinal(), playerFirstCards[position].value.getNumericValue());
			icd.cards = new SchemaCards[] {stCard, null};
			sessionHandler.sendToSessID("GameController/cardsDist", usersInGame[position].sessID, cd); // to the player
			// wait a moment?
			Thread.sleep(250);
		}
		// second iteration:
		for(int position: players) {
			playerSecondCards[position] = deck.getNextCard();
			CardDist cd = new CardDist();
			cd.position = position;
			cd.cards = new boolean[]{ true, true };
			sessionHandler.sendToAll("/GameController/cardsDist", cd); // to all
			ICardDist icd = new ICardDist();
			icd.position = position;
			SchemaCards stCard = new SchemaCards(playerFirstCards[position].suit.ordinal(), playerFirstCards[position].value.getNumericValue());
			SchemaCards ndCard = new SchemaCards(playerSecondCards[position].suit.ordinal(), playerSecondCards[position].value.getNumericValue());
			icd.cards = new SchemaCards[] {stCard, ndCard};
			sessionHandler.sendToSessID("GameController/cardsDist", usersInGame[position].sessID, cd); // to the player
			// wait a moment?
			Thread.sleep(250);
		}
	}
	
	private void dealFlop() {
		deck.getNextCard(); // burn a card 
		Card[] flop = new Card[3];
		flop[0] = deck.getNextCard();
		flop[1] = deck.getNextCard();
		flop[2] = deck.getNextCard();
	}
	
	private void dealTurn() {
		deck.getNextCard(); // burn a card 
		Card turn = deck.getNextCard();
	}
	
	private void dealRiver() {
		deck.getNextCard(); // burn a card 
		Card turn = deck.getNextCard();
	}

	public static SessionHandlerInt getSessionHandler() {
		return sessionHandler;
	}

	public static void setSessionHandler(SessionHandlerInt sessionHandler) {
		RoundGame.sessionHandler = sessionHandler;
	}
	
	public long getRounds() {
		return rounds;
	}

}
