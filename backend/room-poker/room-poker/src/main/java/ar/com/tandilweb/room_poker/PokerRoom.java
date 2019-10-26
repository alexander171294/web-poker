package ar.com.tandilweb.room_poker;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.tandilweb.exchange.gameProtocol.SchemaGameProto;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.StartGame;
import ar.com.tandilweb.room_int.GameCtrlInt;
import ar.com.tandilweb.room_int.handlers.SessionHandlerInt;
import ar.com.tandilweb.room_int.handlers.dto.UserData;
import ar.com.tandilweb.room_poker.deck.Deck;

public class PokerRoom implements GameCtrlInt {
	
	private static final Logger log = LoggerFactory.getLogger(PokerRoom.class);
	private UserData[] usersInTable;
	private int tableSize;
	private SessionHandlerInt sessionHandler;
	private int dealerPosition;
	
	public boolean inGame;
	

	public void setUsersInTableRef(UserData[] usersInTable, SessionHandlerInt sessionHandler) {
		log.debug("Set Users In Table Ref");
		this.usersInTable = usersInTable;
		this.sessionHandler = sessionHandler;
		RoundGame.setSessionHandler(sessionHandler);
		tableSize = usersInTable.length;
		this.inGame = false;
	}

	public void checkStartGame() {
		log.debug("Check Start Game");
		if(!inGame && Utils.checkPlayers(usersInTable) >= 1) { // FIXME: change this to 2 or get from configuration, is in one only for test/dev purposes.
			// START GAME
			log.debug("START GAME");
			this.inGame = true;
			// start game:
			final StartGame startGame = new StartGame();
			startGame.startIn = 30; // initial time
			//final SessionHandlerInt _sessionHandler = sessionHandler;
			final Timer timer = new Timer("StartGameTimmer");
			TimerTask timeToStartGame = new TimerTask() {
				public void run() {
					startGame.startIn -= 10;
					if(startGame.startIn <= 0) {
						timer.cancel();
						realStartGame();
					} else {
						sessionHandler.sendToAll("/GameController/startGame", startGame);
						log.debug("Start game in: " + startGame.startIn);
					}
				}
			};
			timer.scheduleAtFixedRate(timeToStartGame, 10000L, 10000L);
			log.debug("Start game in: " + startGame.startIn);
			sessionHandler.sendToAll("/GameController/startGame", startGame);
		}
	}
	
	public void realStartGame() {
		log.debug("Starting game.");
		// define the dealer
		this.dealerPosition = Utils.getRandomPositionOfTakens(usersInTable);
		startRound();
	}
	
	private void startRound() {
		UserData[] usersInGame = Utils.getNewArrayOfUsers(usersInTable);
		this.dealerPosition = Utils.getNextPositionOfPlayers(usersInGame, this.dealerPosition);
		RoundGame round = new RoundGame(new Deck(), usersInGame, this.dealerPosition);
		round.start();
	}

	public void dumpSnapshot() {
		// TODO Auto-generated method stub
		log.debug("Dump Snapshot");
	}
	
	public void receivedMessage(SchemaGameProto message, String socketSessionID) {
		// TODO Auto-generated method stub
		log.debug("Receive message from " + socketSessionID);
	}

	public void onNewPlayerSitdown(UserData player) {
		// TODO Auto-generated method stub
		log.debug("New Player: " + player.userID);
	}

	public void onDeposit(UserData player, long chipsDeposited) {
		// TODO Auto-generated method stub
		log.debug("New Deposit to: " + player.userID + " chips: " + chipsDeposited);
	}
	
}
