package ar.com.tandilweb.room_poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.generic.Winner;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.ActionFor;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.BetDecision;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.Blind;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.CardDist;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.DecisionInform;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.FlopBegins;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.ICardDist;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.Pots;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.ResultSet;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.RiverBegins;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.RoundStart;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.SchemaCard;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.ShowOff;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.TurnBegins;
import ar.com.tandilweb.room_int.handlers.SessionHandlerInt;
import ar.com.tandilweb.room_int.handlers.dto.UserData;
import ar.com.tandilweb.room_poker.deck.Deck;
import ar.com.tandilweb.room_poker.deck.cards.Card;
import ar.com.tandilweb.room_poker.deck.hands.HandValues;
import ar.com.tandilweb.room_poker.roundGame.Pot;
import ar.com.tandilweb.room_poker.roundGame.UserMetaData;

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
	private UserMetaData[] usersInGameDescriptor; // this used for add info to userdata in this round.
	private long[] bets;
	private Card[] playerFirstCards;
	private Card[] playerSecondCards;
	private Card[] flop;
	private Card turn;
	private Card river;
	private HandValues[] hands;
	
	private int roundStep;
	private int dealerPosition;
	private int waitingActionFromPlayer; // id of player are waiting.
	private int lastActionedPosition; // for cut actions.
	private int bigBlind;
	private long lastRise;
	private List<Pot> pots = new ArrayList<Pot>();
	
	private Deck deck;
	private boolean isWaiting = false;
	
	private int lastActivePositionDetected;
	
	public RoundGame(Deck deck, UserData[] usersInGame, int dealerPosition) {
		this.usersInGame = usersInGame;
		this.usersInGameDescriptor = new UserMetaData[this.usersInGame.length];
		for(int i = 0; i<this.usersInGame.length; i++) {
			if(this.usersInGame[i] != null) {
				this.usersInGameDescriptor[i] = new UserMetaData();
			}
		}
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
//		bets[smallBlind] = smallBlindSize;
//		bets[bigBlind] = bigBlindSize;
		var pot = new Pot();
		pot.pot += smallBlindSize;
		pot.pot += bigBlindSize;
		pots.add(pot);
		sessionHandler.sendToAll("/GameController/blind", blindObject);
		waitingActionFromPlayer = Utils.getNextPositionOfPlayers(usersInGame, bigBlind);
		lastActionedPosition = bigBlind;
	}
	
	private void sendWaitAction() {
		isWaiting = true;
		ActionFor aFor = new ActionFor();
		aFor.position = waitingActionFromPlayer;
		aFor.remainingTime = 30; // TODO: adjust according to configuration.
		sessionHandler.sendToAll("/GameController/actionFor", aFor);
		// send wait for bet decision:
		sessionHandler.sendToSessID("GameController/betDecision", usersInGame[aFor.position].sessID, calcDecision());
		// TODO: implement timer for wait stop.
	}
	
	public BetDecision calcDecision() {
		// action for:
		BetDecision bd = new BetDecision();
		bd.toCall = lastRise - bets[waitingActionFromPlayer];
		bd.canCheck = bd.toCall == 0;
		bd.minRaise = MIN_RAISE;
		bd.maxRaise = MAX_RAISE;
		return bd;
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
				SchemaCard stCard = Utils.getSchemaFromCard(playerFirstCards[position]);
				icd.cards = new SchemaCard[] {stCard, null};
				sessionHandler.sendToSessID("GameController/cardsDist", usersInGame[position].sessID, icd); // to the player
				// wait a moment?
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
				SchemaCard stCard = Utils.getSchemaFromCard(playerFirstCards[position]);
				SchemaCard ndCard = Utils.getSchemaFromCard(playerSecondCards[position]);
				icd.cards = new SchemaCard[] {stCard, ndCard};
				sessionHandler.sendToSessID("GameController/cardsDist", usersInGame[position].sessID, icd); // to the player
				// wait a moment?
			}
		} catch(NullPointerException npe) {
			log.debug("npe: " + lastPosition, npe);
		}
		
	}
	
	// Return if the round is finished
	public boolean processDecision(DecisionInform dI, UserData uD) {
		dI.position = Utils.getPlyerPosition(usersInGame, uD);
		isWaiting = false;
		if(dI.position.intValue() == waitingActionFromPlayer) {
			boolean actionDoed = false;
			boolean finishedBets = false;
			// check if zero:
			if("raise".equalsIgnoreCase(dI.action) && dI.ammount <= 0) {
				dI.action = "call";	
			}
			if("fold".equalsIgnoreCase(dI.action)) {
				usersInGame[dI.position.intValue()] = null; // fold user.
				if(checkPlayerActives() > 1) {
					actionDoed = true;
				} else {
					return finishBetFullFold();
				}
			}
			if("call".equalsIgnoreCase(dI.action)) {
				long realBet = lastRise - bets[dI.position.intValue()];
				if(usersInGame[dI.position.intValue()].chips >= realBet) {
					usersInGame[dI.position.intValue()].chips -= realBet;
					if(usersInGame[dI.position.intValue()].chips == 0) {
						this.usersInGameDescriptor[dI.position.intValue()].isAllIn = true;
					}
					actionDoed = true;
					dI.ammount = realBet; // change the ammount to real count for frontend
					bets[dI.position.intValue()] = lastRise;
				} else {
					// TODO: review this.
					dI.ammount = usersInGame[dI.position.intValue()].chips;
					this.usersInGameDescriptor[dI.position.intValue()].isAllIn = true;
					actionDoed = true;
					bets[dI.position.intValue()] += usersInGame[dI.position.intValue()].chips;
					usersInGame[dI.position.intValue()].chips = 0;
				}
			}
			if("check".equalsIgnoreCase(dI.action)) {
				if(lastRise == bets[dI.position.intValue()]) {
					actionDoed = true;
				}
			}
			if("raise".equalsIgnoreCase(dI.action)) {
				// TODO: check maximums and minimums.
				long ammount = dI.ammount;
				long initialBet = lastRise - bets[dI.position.intValue()];
				lastActionedPosition = dI.position.intValue();
				long totalAmmount = initialBet+ammount;
				if(usersInGame[dI.position.intValue()].chips >= totalAmmount) {
					usersInGame[dI.position.intValue()].chips -= totalAmmount;
					if(usersInGame[dI.position.intValue()].chips == 0) {
						this.usersInGameDescriptor[dI.position.intValue()].isAllIn = true;
					}
					dI.ammount = totalAmmount; // change the ammount to real count for frontend
					actionDoed = true;
					bets[dI.position.intValue()] +=  totalAmmount;
					lastRise = bets[dI.position.intValue()];
					bigBlind = -1;
				} else {
					// TODO: Review this.
					bets[dI.position.intValue()] += initialBet+usersInGame[dI.position.intValue()].chips;
					dI.ammount = initialBet+usersInGame[dI.position.intValue()].chips;
					usersInGame[dI.position.intValue()].chips = 0;
					this.usersInGameDescriptor[dI.position.intValue()].isAllIn = true;
					actionDoed = true;
					lastRise = bets[dI.position.intValue()];
					bigBlind = -1;
				}
			}
			
			if(actionDoed) {
				int nextPosition = Utils.getNextPositionOfPlayers(usersInGame, dI.position.intValue());
				if("raise".equalsIgnoreCase(dI.action)) {
					nextPlayer(nextPosition);
				} else {
					if(nextPosition == bigBlind) {
						nextPlayer(nextPosition);
					} else {
						if(isAllinAllIn()) {
							finishedBets = true;
						} else if(lastActionedPosition == nextPosition || dI.position.intValue() == bigBlind) { // if next is last or actual is last (in bigBlind case)
							finishedBets = true;
						} else {
							nextPlayer(nextPosition);
						}
					}
				}
				sessionHandler.sendToAll("/GameController/decisionInform", dI);
				if(finishedBets) {
					return finishBets();
				}
			} else {
				// TODO: error message?
			}
		}
		return false;
	}
	
	private int checkPlayerActives() {
		int count = 0;
		for(int i = 0; i < usersInGame.length; i++) {
			if(usersInGame[i] != null) {
				count++;
				lastActivePositionDetected = i;
			}
		}
		return count;
	}
	
	private boolean finishBetFullFold() {
		// check round bets:
		long pot = 0;
		for(int i = 0; i < bets.length; i++) {
			pot += bets[i];
		}
		for(Pot potObj: pots) {
			pot += potObj.pot;
		}
		ResultSet rs = new ResultSet();
		rs.winners = new ArrayList<Winner>();
		int winner = lastActivePositionDetected;
		Winner winnerData = new Winner();
		winnerData.points = 0;
		winnerData.position = winner;
		winnerData.pot = pot;
		winnerData.reason = "All other players fold";
		usersInGame[winner].chips += pot;
		rs.winners.add(winnerData);
		sessionHandler.sendToAll("/GameController/resultSet", rs);
		threadWait(500); // TODO: parameterize
		return true;
	}
	
	private boolean finishBets() {
		bigBlind = -1;
		int nextPj = Utils.getNextPositionOfPlayers(usersInGame, this.dealerPosition);
		lastActionedPosition = nextPj;
		// resetting rises:
		lastRise = 0;
		// merge de pots:
		List<Pot> newPots = SplitAndNormalizedPots();
		if(newPots.size() > 0) {
			this.pots.get(this.pots.size()-1).pot += newPots.get(0).pot;
			newPots.remove(0);
			this.pots.addAll(newPots);
		}
		// mandamos al front la lista de pots:
		Pots schemaPots = new Pots();
		schemaPots.pots = Utils.getPotValues(pots);
		sessionHandler.sendToAll("/GameController/pots", schemaPots);
		if(roundStep == 1) {
			// flop:
			roundStep = 2;
			// wait a moment?
			threadWait(500); // TODO: parameterize
			dealFlop();
			if(isAllinAllIn()) {
				threadWait(500); // TODO: parameterize
				return finishBets();
			} else {
				nextPlayer(nextPj);	
			}
		} else if(roundStep == 2) {
			// turn:
			// wait a moment?
			threadWait(500); // TODO: parameterize
			roundStep = 3;
			dealTurn();
			if(isAllinAllIn()) {
				threadWait(500); // TODO: parameterize
				return finishBets();
			} else {
				nextPlayer(nextPj);
			}
		} else if(roundStep == 3) {
			// turn:
			// wait a moment?
			threadWait(500); // TODO: parameterize
			roundStep = 4;
			dealRiver();
			if(isAllinAllIn()) {
				threadWait(500); // TODO: parameterize
				return finishBets();
			} else {
				nextPlayer(nextPj);
			}
		} else if(roundStep == 4) {
			log.debug("-- SHOWDOWN --");
			showOff();
			
			// TODO: cambiar esto por multi-pots:
			long fullPot = 0;
			for(long pot: schemaPots.pots) {
				fullPot += pot;
			}
			checkHands(fullPot);
			// fin del todo
			
			threadWait(2500);
			return true;
		}
		return false;
	}
	
	private void showOff() {
		ShowOff soff = new ShowOff(usersInGame.length);
		for(int i = 0; i < usersInGame.length; i++) {
			if(usersInGame[i] != null) {
				soff.setCards(
						i,
						Utils.getSchemaFromCard(playerFirstCards[i]),
						Utils.getSchemaFromCard(playerSecondCards[i])
				);
			}
		}
		sessionHandler.sendToAll("/GameController/showOff", soff);
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
			schemaCards[i] = Utils.getSchemaFromCard(flop[i]);
		}
		fb.cards = schemaCards;
		// flop begins:
		sessionHandler.sendToAll("/GameController/flop", fb);
	}
	
	private void dealTurn() {
		deck.getNextCard(); // burn a card 
		turn = deck.getNextCard();
		TurnBegins tb = new TurnBegins();
		tb.card = Utils.getSchemaFromCard(turn);
		// turn begins:
		sessionHandler.sendToAll("/GameController/turn", tb);
	}
	
	private void dealRiver() {
		deck.getNextCard(); // burn a card 
		river = deck.getNextCard();
		RiverBegins rb = new RiverBegins();
		rb.card = Utils.getSchemaFromCard(river);
		// river begins:
		sessionHandler.sendToAll("/GameController/river", rb);
	}
	
	private void checkHands(long pot) {
		hands = new HandValues[usersInGame.length];
		List<Card> tableCards = new ArrayList<Card>();
		for(int i = 0; i < 3; i++) {
			tableCards.add(flop[i]);
		}
		tableCards.add(turn);
		tableCards.add(river);
		int winner = 0;
		int winnerPoints = 0;
		int winnerSecondaryPoints = 0;
		int winnerKickerPoints = 0;
		boolean dualWinner = false;
		List<Integer> aditionalWinners = new ArrayList<Integer>();
		for(int i = 0; i<usersInGame.length; i++) {
			if(usersInGame[i] != null) {
				List<Card> hand = new ArrayList<Card>();
				hand.add(playerFirstCards[i]);
				hand.add(playerSecondCards[i]);
				hands[i] = deck.getHandData(hand, tableCards);
				// FIXME: improve this ifs.
				if(winnerPoints < hands[i].handPoints) {
					winner = i;
					winnerSecondaryPoints = hands[i].secondaryHandPoint;
					winnerKickerPoints = hands[i].kickerPoint;
					winnerPoints = hands[i].handPoints;
					dualWinner = false;
					aditionalWinners = new ArrayList<Integer>();
				} else if(winner == hands[i].handPoints) {
					if(winnerSecondaryPoints < hands[i].secondaryHandPoint) {
						winner = i;
						winnerSecondaryPoints = hands[i].secondaryHandPoint;
						winnerKickerPoints = hands[i].kickerPoint;
						winnerPoints = hands[i].handPoints;
						dualWinner = false;
						aditionalWinners = new ArrayList<Integer>();
					} else if(winnerSecondaryPoints == hands[i].secondaryHandPoint) {
						if(winnerKickerPoints < hands[i].kickerPoint) {
							winner = i;
							winnerSecondaryPoints = hands[i].secondaryHandPoint;
							winnerKickerPoints = hands[i].kickerPoint;
							winnerPoints = hands[i].handPoints;
							dualWinner = false;
							aditionalWinners = new ArrayList<Integer>();
						} else {
							dualWinner = true;
							aditionalWinners.add(i);
						}
					}
				}
			}
		}
		Double winPot = (double) (pot / (1 + aditionalWinners.size()));
		ResultSet rs = new ResultSet();
		rs.winners = new ArrayList<Winner>();
		Winner winnerData = new Winner();
		winnerData.points = winnerPoints;
		winnerData.position = winner;
		winnerData.pot = winPot.longValue();
		winnerData.reason = hands[winner].handName;
		usersInGame[winner].chips += winPot.longValue();
		rs.winners.add(winnerData);
		if(aditionalWinners.size() > 0) {
			for(int wPos: aditionalWinners) {
				Winner secondaryWinner = new Winner();
				secondaryWinner.points = winnerData.points;
				secondaryWinner.position = wPos;
				secondaryWinner.pot = winPot.longValue();
				usersInGame[wPos].chips += winPot.longValue();
				secondaryWinner.reason = hands[wPos].handName;
				rs.winners.add(secondaryWinner);
			}
		}
		sessionHandler.sendToAll("/GameController/resultSet", rs);
	}
	
	public List<Pot> SplitAndNormalizedPots() {
		// TODO: chequear y remover los folds primero
		
		// separamos los pozos:
		List<Pot> pozos = new ArrayList<Pot>();
		List<Integer> activeUsers = Utils.getPlayersOrderedByBets(bets);
		if(activeUsers.size() > 1) {
			List<Integer> activeUsersWithoutBigBet = Utils.getPlayersOrderedByBets(bets);
			activeUsersWithoutBigBet.remove(activeUsersWithoutBigBet.size()-1);
			for(int i = 0; i<=activeUsersWithoutBigBet.size()-1; i++) {
				// restamos el bet de esta posicion a las siguientes:
				final var index = activeUsersWithoutBigBet.get(i);
				if(bets[index] <= 0) continue;
				Pot pozo = new Pot();
				pozo.pot = 0;
				long bet = bets[index];
				for(int z = i; z<=activeUsers.size()- 1; z++) {
					final var zindex = activeUsers.get(z);
					bets[zindex] -= bet;
					pozo.pot += bet;
					pozo.playersForPot.add(zindex);
				}
				pozos.add(pozo);
				boolean morePots = false;
				for(int z = 0; z<=activeUsersWithoutBigBet.size()-1; z++) {
					final var zindex = activeUsers.get(z);
					if(bets[zindex]>0) {
						morePots = true;
					}
				}
				if(!morePots) {
					break;
				}
			}
			// devolver excedente del mas grande:
			var maxBexPosition = activeUsers.get(activeUsers.size()-1);
			var excedent = bets[maxBexPosition];
			usersInGame[maxBexPosition].chips += excedent;
		}
		return pozos;
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
	
	public int getDealerPosition() {
		return dealerPosition;
	}
	
	public List<Pot> getPot() {
		return this.pots;
	}
	
	public long getBetOf(int position) {
		return bets[position];
	}
	
	public int getStep() {
		return roundStep;
	}
	
	public int getWaitingActionFromPlayer() {
		return waitingActionFromPlayer;
	}
	
	public boolean checkWaiting() {
		return isWaiting;
	}
	
	public Card[] getCommunityCards() {
		if(roundStep == 2) {
			return flop;
		}
		if(roundStep == 3) {
			return ArrayUtils.addAll(flop, turn);
		}
		if(roundStep == 4) {
			return ArrayUtils.addAll(flop, turn, river);
		}
		return new Card[]{};
	}
	
	public void threadWait(long time) {
		// FIXME: fix this:
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {

		}
	}
	
	public SchemaCard[] getCards(int pos) {
		return new SchemaCard[] { Utils.getSchemaFromCard(playerFirstCards[pos]), Utils.getSchemaFromCard(playerSecondCards[pos]) };
	}
	
	public boolean isAllinAllIn() {
		int usersInGameNotAllIn = 0;
		for(int i = 0; i < this.usersInGame.length; i++) {
			if(this.usersInGame[i] != null && !this.usersInGameDescriptor[i].isAllIn) {
				usersInGameNotAllIn++;
			}
		}
		return usersInGameNotAllIn <= 1;
	}
	
	public static int getBigBlind() {
		return BIG_BLIND;
	}
	
	public static int getSmallBlind() {
		return SMALL_BLIND;
	}

}
