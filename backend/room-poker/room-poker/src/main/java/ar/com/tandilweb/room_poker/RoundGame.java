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
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.FoldDecision;
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
import ar.com.tandilweb.room_poker.deck.hands.HandType;
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
	
	public boolean start() {
		RoundStart roundStartSchema = new RoundStart();
		roundStartSchema.dealerPosition = this.dealerPosition;
		roundStartSchema.roundNumber = rounds;
		sessionHandler.sendToAll("/GameController/roundStart", roundStartSchema);
		boolean AllInCornerCase = requestBlind(SMALL_BLIND, BIG_BLIND); // FXIME: adjust according to configuration.
		try {
			dealCards();
			roundStep = 1; // pre-flop
			if(!AllInCornerCase) {				
				sendWaitAction();
				return false;
			} else {
				showOff();
				threadWait(500);
				return finishBets();
			}
		} catch (InterruptedException e) {
			log.warn("Interrupted Exception ", e);
			// FIXME: if this explode, then the cards are never ends to dealing.
		}
		return false;
	}
	
	private boolean requestBlind(int smallBlindSize, int bigBlindSize) {
		int smallBlind = Utils.getNextPositionOfPlayers(usersInGame, this.dealerPosition);
		bigBlind = Utils.getNextPositionOfPlayers(usersInGame, smallBlind);
		lastRise = bigBlindSize;
		Blind blindObject = new Blind();
		blindObject.sbPosition = smallBlind;
		blindObject.sbChips = smallBlindSize;
		blindObject.bbPosition = bigBlind;
		blindObject.bbChips = bigBlindSize;
		
		if(usersInGame[smallBlind].chips > smallBlindSize) {
			usersInGame[smallBlind].chips -= smallBlindSize;
			bets[smallBlind] = smallBlindSize;
		} else {
			// ALL IN
			bets[smallBlind] = usersInGame[smallBlind].chips;
			blindObject.sbChips = bets[smallBlind];
			usersInGameDescriptor[smallBlind].isAllIn = true;
			usersInGame[smallBlind].chips = 0;
		}
		
		if(usersInGame[bigBlind].chips > bigBlindSize) {
			usersInGame[bigBlind].chips -= bigBlindSize;
			bets[bigBlind] = bigBlindSize;
		} else {
			// ALL IN
			bets[bigBlind] = usersInGame[bigBlind].chips;
			blindObject.bbChips = bets[bigBlind];
			usersInGameDescriptor[bigBlind].isAllIn = true;
			usersInGame[bigBlind].chips = 0;
		}

		sessionHandler.sendToAll("/GameController/blind", blindObject);
		
		
		int firstWAFP = -1;
		waitingActionFromPlayer = Utils.getNextPositionOfPlayers(usersInGame, bigBlind);
		while(usersInGameDescriptor[waitingActionFromPlayer].isAllIn && waitingActionFromPlayer != firstWAFP) {
			if(firstWAFP == -1) {
				firstWAFP = waitingActionFromPlayer;
			}
			waitingActionFromPlayer = Utils.getNextPositionOfPlayers(usersInGame, bigBlind);
		}
		if(usersInGameDescriptor[waitingActionFromPlayer].isAllIn) {
			// todos en allIn?
			return true;
		}
		if(((waitingActionFromPlayer == bigBlind) || (
			waitingActionFromPlayer == smallBlind &&
			blindObject.bbChips == smallBlindSize)) && 
				isAllinAllIn()) {
			// automaticamente cerrar el juego como si todos fueran all in
			return true;
		}
		
		lastActionedPosition = bigBlind;
		return false;
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
				// TODO: remove me from all pots winners.
				usersInGame[dI.position.intValue()] = null; // fold user.
				FoldDecision fd = new FoldDecision();
				fd.position = dI.position.intValue();
				sessionHandler.sendToAll("/GameController/fold", fd);
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
					//lastActionedPosition = dI.position.intValue();
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
					bets[dI.position.intValue()] += usersInGame[dI.position.intValue()].chips;
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
					finishedBets = nextPlayer(nextPosition);
				} else {
					if(isAllinAllIn()) {
						finishedBets = true;
						showOff();
						threadWait(500); // TODO: parametize this
					} else {
						if(nextPosition == bigBlind) {
							finishedBets = nextPlayer(nextPosition);
						} else if(lastActionedPosition == nextPosition || dI.position.intValue() == bigBlind) { // if next is last or actual is last (in bigBlind case)
							finishedBets = true;
						} else {
							finishedBets = nextPlayer(nextPosition);
						}
					}
					
//					if(nextPosition == bigBlind) {
//						finishedBets = nextPlayer(nextPosition);
//					} else {
//						if(isAllinAllIn()) {
//							finishedBets = true;
//							showOff();
//							threadWait(500); // TODO: parametize this
//						} else if(lastActionedPosition == nextPosition || dI.position.intValue() == bigBlind) { // if next is last or actual is last (in bigBlind case)
//							finishedBets = true;
//						} else {
//							finishedBets = nextPlayer(nextPosition);
//						}
//					}
					
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
			if(this.pots.size() > 0) {
				this.pots.get(this.pots.size()-1).pot += newPots.get(0).pot;
				newPots.remove(0);
				this.pots.addAll(newPots);
			} else {
				this.pots.addAll(newPots);
			}
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
			
			checkHands(pots);
			
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
	
	private boolean nextPlayer(int nextPosition) {
		if(usersInGameDescriptor[nextPosition].isAllIn) {
			do {
				nextPosition = Utils.getNextPositionOfPlayers(usersInGame, nextPosition);
			} while(usersInGameDescriptor[nextPosition].isAllIn && nextPosition != lastActionedPosition);
			if(nextPosition == lastActionedPosition) {
				return true; // fin de la mano
			} else {
				waitingActionFromPlayer = nextPosition;
				sendWaitAction();
			}
		} else {			
			waitingActionFromPlayer = nextPosition;
			sendWaitAction();
		}
		return false;
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
	
	private void checkHands(List<Pot> pots) {
		hands = new HandValues[usersInGame.length];
		List<Card> tableCards = new ArrayList<Card>();
		for(int i = 0; i < 3; i++) {
			tableCards.add(flop[i]);
		}
		tableCards.add(turn);
		tableCards.add(river);
		for(int i = 0; i<usersInGame.length; i++) {
			if(usersInGame[i] != null) {
				List<Card> hand = new ArrayList<Card>();
				hand.add(playerFirstCards[i]);
				hand.add(playerSecondCards[i]);
				hands[i] = deck.getHandData(hand, tableCards);
			}
		}
//		log.debug("Hands: ", hands);
		List<Winner> winners = new ArrayList<Winner>();
		List<Winner> prevWinner = null;
		int iteration = 0;
		for(var pot: pots) {
			prevWinner = getWinnerOf(pot, prevWinner, iteration);
			iteration++;
			winners.addAll(prevWinner);
		}
		ResultSet rs = new ResultSet();
		rs.winners = winners;
		sessionHandler.sendToAll("/GameController/resultSet", rs);
	}
	
	public List<Winner> getWinnerOf(Pot pot, List<Winner> prevWinner, int iteration) {
		List<Winner> winnersPositions = new ArrayList<Winner>();
		// prev winner for this pot:
		// TODO: prevenir recalcular revisando si los ganadores previos también son ganadores de este
		//
		int maxPoints = 0;
		int secondaryMaxPoints = 0;
		HandType handWinner = null;
		// traemos los ganadores por jerarquía
		for(int player: pot.playersForPot) {
			if(hands[player] == null) {
				continue;
			}
			if(hands[player].handPoints > maxPoints) {
				Winner winData = new Winner();
				winnersPositions = new ArrayList<Winner>();
				winData.fullPot = pot.pot;
				winData.points = hands[player].handPoints;
				winData.position = player;
				winData.reason = hands[player].handName;
				winData.secondaryPoints = hands[player].secondaryHandPoint;
				winData.potNumber = iteration;
				maxPoints = hands[player].handPoints;
				secondaryMaxPoints = hands[player].secondaryHandPoint;
				handWinner = hands[player].type;
				winnersPositions.add(winData);
			} else if(hands[player].handPoints == maxPoints) {
				Winner winData = new Winner();
				winData.fullPot = pot.pot;
				winData.points = hands[player].handPoints;
				winData.secondaryPoints = hands[player].secondaryHandPoint;
				winData.position = player;
				winData.potNumber = iteration;
				winData.reason = hands[player].handName;
				// juegos con doble handPoint como full o par doble que tienen puntos secundarios:
				if(handWinner == HandType.FULL_HOUSE || handWinner == HandType.TWO_PAIRS) {
					// validamos que el secondary points sea iguales también
					if(hands[player].secondaryHandPoint > secondaryMaxPoints) {
						winnersPositions = new ArrayList<Winner>();
						winnersPositions.add(winData);
						secondaryMaxPoints = hands[player].secondaryHandPoint;
					} else if(hands[player].secondaryHandPoint == secondaryMaxPoints) {
						winnersPositions.add(winData);
					}
				} else {					
					winnersPositions.add(winData);
				}
			}
		}
		// check kickers:
		// juegos que tienen menos de 5 cartas para contar kicker:
		int bigKicker = 0;
		int smallKicker = 0;
		List<Winner> cleanWinnersPositions = new ArrayList<Winner>();
		if(winnersPositions.size() > 1 && handWinner != HandType.FULL_HOUSE && handWinner != HandType.FLUSH && handWinner != HandType.STRAIGHT && handWinner != HandType.STRAIGHT_FLUSH) {			
			for(var winner: winnersPositions) {
				// no tengo kicker:
				if(hands[winner.position].kickerPoint.size() == 0) {
					// el kicker no existe
					if(bigKicker == 0) {						
						bigKicker = 0;
						smallKicker = 0;
						cleanWinnersPositions.add(winner);
					}
				} else {
					// tengo kicker
					// mi kicker es mejor que el del otro
					if(hands[winner.position].kickerPoint.get(0) > bigKicker) {
						cleanWinnersPositions = new ArrayList<Winner>();
						bigKicker = hands[winner.position].kickerPoint.get(0);
						smallKicker = hands[winner.position].kickerPoint.size() > 1 ? hands[winner.position].kickerPoint.get(1) : 0;
						cleanWinnersPositions.add(winner);
					// mi kicker es igual al del otro
					} else if(hands[winner.position].kickerPoint.get(0) == bigKicker) { 
						// no tengo segundo kicker:
						if(hands[winner.position].kickerPoint.size() < 2) {
							// el otro tampoco tiene segundo kicker:
							if(smallKicker == 0) {								
								bigKicker = 0;
								smallKicker = 0;
								cleanWinnersPositions.add(winner);
							}
						} else { // tengo segundo kicker
							// mi segundo kicker es más grande que el del otro:
							if(hands[winner.position].kickerPoint.get(1) > smallKicker) {
								cleanWinnersPositions = new ArrayList<Winner>();
								bigKicker = hands[winner.position].kickerPoint.get(0);
								smallKicker = hands[winner.position].kickerPoint.get(1);
								cleanWinnersPositions.add(winner);
							} else if(hands[winner.position].kickerPoint.get(1) == smallKicker) { // mi segundo kicker es igual al del otro
								cleanWinnersPositions.add(winner);
							}
						}
					}
				}
			}
		} else {
			cleanWinnersPositions = winnersPositions;
		}
		
		// calcular pots
		final int countWinners = cleanWinnersPositions.size();
		cleanWinnersPositions.forEach(winner -> {
			winner.pot = winner.fullPot / countWinners;
			usersInGame[winner.position].chips += winner.pot;
		});
		return cleanWinnersPositions;
	}
	
	public List<Pot> SplitAndNormalizedPots() {
		// chequeamos y removemos los folds primero
		long[] betsWithoutFolds = new long[usersInGame.length];
		for(int i = 0; i < usersInGame.length; i++) {
			if(usersInGame[i] != null) {
				betsWithoutFolds[i] = bets[i];
			}
		}
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
			log.error("INTERRUPTED EXCEPTION", e);
		}
	}
	
	public SchemaCard[] getCards(int pos) {
		if(playerFirstCards[pos] == null) {
			return null;
		}
		return new SchemaCard[] { Utils.getSchemaFromCard(playerFirstCards[pos]), Utils.getSchemaFromCard(playerSecondCards[pos]) };
	}
	
	public boolean haveCards(int pos) {
		return playerFirstCards[pos] != null;
	}
	
	public boolean isInGame(int pos) {
		return usersInGame[pos] != null;
	}
	
	public boolean isAllinAllIn() {
		int usersInGameNotAllIn = 0;
		int userPending = 0;
		long maxBet = 0;
		int maxBeter = 0;
		for(int i = 0; i < this.usersInGame.length; i++) {
			if(this.usersInGame[i] != null && !this.usersInGameDescriptor[i].isAllIn) {
				usersInGameNotAllIn++;
				userPending = i;
			} else if(this.usersInGame[i] != null){
				if(bets[i] > maxBet) {
					maxBet = bets[i];
					maxBeter = i;
				}
			}
		}
		if(usersInGameNotAllIn == 0) {
			return true;
		}
		if(usersInGameNotAllIn == 1) {
			return bets[userPending] >= bets[maxBeter];
		}
		return false;
	}
	
	public static int getBigBlind() {
		return BIG_BLIND;
	}
	
	public static int getSmallBlind() {
		return SMALL_BLIND;
	}

}
