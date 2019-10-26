package ar.com.tandilweb.room_poker;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.ActionFor;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.BetDecision;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.Blind;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.RoundStart;
import ar.com.tandilweb.room_int.handlers.SessionHandlerInt;
import ar.com.tandilweb.room_int.handlers.dto.UserData;
import ar.com.tandilweb.room_poker.deck.Deck;

public class RoundGame {
	
	private static SessionHandlerInt sessionHandler;
	private static long rounds = 0;
	
	private UserData[] usersInGame;
	private long[] bets;
	private int dealerPosition;
	private int tableSize;
	private int waitingActionFromPlayer; // id of player are waiting.
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
	}
	
	public void requestBlind(int smallBlindSize, int bigBlindSize) {
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
		ActionFor aFor = new ActionFor();
		aFor.position = waitingActionFromPlayer;
		aFor.remainingTime = 30; // FIXME: adjust according to configuration.
		sessionHandler.sendToAll("/GameController/actionFor", aFor);
		// action for:
		BetDecision bd = new BetDecision();
		bd.toCall = bigBlindSize;
		bd.canCheck = false;
		bd.minRaise = bigBlindSize; // FIXME: adjust according to configuration.
		bd.maxRaise = -1; // FIXME: adjust according to configuration.
		// send wait for bet decision:
		sessionHandler.sendToSessID("gameController/betDecision", usersInGame[aFor.position].sessID, bd);
		// TODO: implement timer for wait stop.
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
