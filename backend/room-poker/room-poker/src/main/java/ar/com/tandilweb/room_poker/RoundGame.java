package ar.com.tandilweb.room_poker;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.ActionFor;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.BetDecision;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.Blind;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.CardDist;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.DecisionInform;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.FlopBegins;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.ICardDist;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.RiverBegins;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.RoundStart;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.SchemaCard;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.TurnBegins;
import ar.com.tandilweb.room_int.handlers.SessionHandlerInt;
import ar.com.tandilweb.room_int.handlers.dto.UserData;
import ar.com.tandilweb.room_poker.deck.Deck;
import ar.com.tandilweb.room_poker.deck.cards.Card;

public class RoundGame {
	
	private static final Logger log = LoggerFactory.getLogger(RoundGame.class);
	
	private static SessionHandlerInt sessionHandler;
	private static long rounds = 0;
	
	private static final int MIN_RAISE = 50; // TODO: adjust according to configuration.
	private static final int MAX_RAISE = -1; // TODO: adjust according to configuration.
	private static int SMALL_BLIND = 25; // TODO: adjust according to configuration.
	private static int BIG_BLIND = 50; // TODO: adjust according to configuration.
	private static final int BLIND_MULTIPLIER = 2; // TODO: adjust according to configuration.
	
	// player datas:
	private UserData[] usersInGame;
	private long[] bets;
	private Card[] playerFirstCards;
	private Card[] playerSecondCards;
	private Card[] flop;
	private Card turn;
	private Card river;
	
	private int roundStep;
	private int dealerPosition;
	private int tableSize;
	private int waitingActionFromPlayer; // id of player are waiting.
	private int lastActionedPosition; // for cut actions.
	private int bigBlind;
	private long lastRise;
	
	private Deck deck;
	
	public RoundGame(Deck deck, UserData[] usersInGame, int dealerPosition) {
		this.usersInGame = usersInGame;
		this.bets = ArrayUtils.toPrimitive(Collections.nCopies(usersInGame.length, 0L).toArray(new Long[0]));
		this.playerFirstCards = new Card[usersInGame.length];
		this.playerSecondCards = new Card[usersInGame.length];
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
		requestBlind(SMALL_BLIND, BIG_BLIND); // FXIME: adjust according to configuration.
		try {
			dealCards();
			roundStep = 1; // pre-flop
			sendWaitAction();
		} catch (InterruptedException e) {
			log.warn("Interrupted Exception ", e);
			// FIXME: if this explode, then the cards are never ends to dealing.
		}
	}
	
	private void requestBlind(int smallBlindSize, int bigBlindSize) {
		int smallBlind = Utils.getNextPositionOfPlayers(usersInGame, this.dealerPosition);
		bigBlind = Utils.getNextPositionOfPlayers(usersInGame, smallBlind);
		lastRise = bigBlindSize;
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
	
	private void sendWaitAction() {
		ActionFor aFor = new ActionFor();
		aFor.position = waitingActionFromPlayer;
		aFor.remainingTime = 30; // TODO: adjust according to configuration.
		sessionHandler.sendToAll("/GameController/actionFor", aFor);
		// action for:
		BetDecision bd = new BetDecision();
		bd.toCall = lastRise - bets[waitingActionFromPlayer];
		bd.canCheck = bd.toCall == 0;
		bd.minRaise = MIN_RAISE; 
		bd.maxRaise = MAX_RAISE; 
		// send wait for bet decision:
		sessionHandler.sendToSessID("GameController/betDecision", usersInGame[aFor.position].sessID, bd);
		// TODO: implement timer for wait stop.
	}
	
	private void dealCards() throws InterruptedException {
		List<Integer> players = Utils.getPlayersFromPosition(usersInGame, this.dealerPosition);
		//deck.getNextCard(); // burn a card ?.
		// first iteration:
		int lastPosition = 1;
		try {
			for(int position: players) {
				playerFirstCards[position] = deck.getNextCard();
				CardDist cd = new CardDist();
				cd.position = position;
				cd.cards = new boolean[]{ true, false };
				sessionHandler.sendToAll("/GameController/cardsDist", cd); // to all
				ICardDist icd = new ICardDist();
				icd.position = position;
				lastPosition = position;
				SchemaCard stCard = new SchemaCard(playerFirstCards[position].suit.ordinal(), playerFirstCards[position].value.getNumericValue());
				icd.cards = new SchemaCard[] {stCard, null};
				sessionHandler.sendToSessID("GameController/cardsDist", usersInGame[position].sessID, icd); // to the player
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
				lastPosition = position;
				SchemaCard stCard = new SchemaCard(playerFirstCards[position].suit.ordinal(), playerFirstCards[position].value.getNumericValue());
				SchemaCard ndCard = new SchemaCard(playerSecondCards[position].suit.ordinal(), playerSecondCards[position].value.getNumericValue());
				icd.cards = new SchemaCard[] {stCard, ndCard};
				sessionHandler.sendToSessID("GameController/cardsDist", usersInGame[position].sessID, icd); // to the player
				// wait a moment?
				Thread.sleep(250);
			}
		} catch(NullPointerException npe) {
			
			log.debug("npe: " + lastPosition, npe);
		}
		
	}
	
	public void processDecision(DecisionInform dI, UserData uD) {
		int position = Utils.getPlyerPosition(usersInGame, uD);
		if(position == waitingActionFromPlayer) {
			boolean actionDoed = false;
			if("fold".equalsIgnoreCase(dI.action)) {
				usersInGame[position] = null; // fold user.
				actionDoed = true;
			}
			if("call".equalsIgnoreCase(dI.action)) {
				long realBet = lastRise - bets[position];
				// TODO: splitted POT
				if(usersInGame[position].chips >= realBet) {
					usersInGame[position].chips -= realBet;
					actionDoed = true;
					bets[position] = lastRise;
				}
			}
			if("check".equalsIgnoreCase(dI.action)) {
				if(lastRise == bets[position]) {
					actionDoed = true;
				}
			}
			if("raise".equalsIgnoreCase(dI.action)) {
				// TODO: check maximums and minimums.
				long ammount = dI.ammount;
				long initialBet = lastRise - bets[position];
				lastActionedPosition = position;
				long totalAmmount = initialBet+ammount;
				if(usersInGame[position].chips >= totalAmmount) {
					usersInGame[position].chips -= totalAmmount;
					actionDoed = true;
					bets[position] +=  totalAmmount;
					lastRise = bets[position];
					bigBlind = -1;
				}
			}
			
			if(actionDoed) {
				int nextPosition = Utils.getNextPositionOfPlayers(usersInGame, position);
				if("raise".equalsIgnoreCase(dI.action)) {
					nextPlayer(nextPosition);
				} else {
					if(nextPosition == bigBlind) {
						nextPlayer(nextPosition);
					} else {
						if(lastActionedPosition == nextPosition || position == bigBlind) { // if next is last or actual is last (in bigBlind case)
							finishBets();
						} else {
							nextPlayer(nextPosition);
						}
					}
				}
				sessionHandler.sendToAll("/GameController/DecisionInform", dI);
			} else {
				// TODO: error message?
			}
		}
	}
	
	private void finishBets() {
		bigBlind = -1;
		int nextPj = Utils.getNextPositionOfPlayers(usersInGame, this.dealerPosition);
		lastActionedPosition = nextPj;
		if(roundStep == 1) {
			// flop:
			roundStep = 2;
			dealFlop();
			nextPlayer(nextPj);
		} else if(roundStep == 2) {
			// turn:
			roundStep = 3;
			dealTurn();
			nextPlayer(nextPj);
		} else if(roundStep == 3) {
			// turn:
			roundStep = 4;
			dealRiver();
			nextPlayer(nextPj);
		} else if(roundStep == 4) {
			// showdown
			log.debug("!!! SHOWDOWN !!!");
		}
	}
	
	private void nextPlayer(int nextPosition) {
		waitingActionFromPlayer = nextPosition;
		sendWaitAction();
	}
	
	private void dealFlop() {
		deck.getNextCard(); // burn a card 
		FlopBegins fb = new FlopBegins();
		SchemaCard[] schemaCards = new SchemaCard[3];
		flop = new Card[3];
		for(int i = 0; i<3; i++) {
			flop[i] = deck.getNextCard();
			schemaCards[i] = new SchemaCard(flop[i].suit.ordinal(), flop[i].value.getNumericValue());
		}
		fb.cards = schemaCards;
		// flop begins:
		sessionHandler.sendToAll("/GameController/flop", fb);
	}
	
	private void dealTurn() {
		deck.getNextCard(); // burn a card 
		turn = deck.getNextCard();
		TurnBegins tb = new TurnBegins();
		tb.card = new SchemaCard(turn.suit.ordinal(), turn.value.getNumericValue());
		// turn begins:
		sessionHandler.sendToAll("/GameController/turn", tb);
	}
	
	private void dealRiver() {
		deck.getNextCard(); // burn a card 
		river = deck.getNextCard();
		RiverBegins rb = new RiverBegins();
		rb.card = new SchemaCard(river.suit.ordinal(), river.value.getNumericValue());
		// river begins:
		sessionHandler.sendToAll("/GameController/river", rb);
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
	
	public static void increaseBlind() {
		SMALL_BLIND *= BLIND_MULTIPLIER;
		BIG_BLIND *= BLIND_MULTIPLIER;
	}

}
