package ar.com.tandilweb.room_poker;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.com.tandilweb.exchange.gameProtocol.SchemaGameProto;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.accessing.RequestDeposit;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.accessing.Snapshot;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.accessing.SnapshotPlayer;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.ChatMessage;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.DecisionInform;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.DepositAnnouncement;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.LeaveNotify;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.SchemaCard;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.SnapshotRequest;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.inGame.StartGame;
import ar.com.tandilweb.room_int.GameCtrlInt;
import ar.com.tandilweb.room_int.OrchestratorPipe;
import ar.com.tandilweb.room_int.handlers.SessionHandlerInt;
import ar.com.tandilweb.room_int.handlers.dto.UserData;
import ar.com.tandilweb.room_poker.deck.Deck;
import ar.com.tandilweb.room_poker.deck.cards.Card;

public class PokerRoom implements GameCtrlInt {
	
	private static final Logger log = LoggerFactory.getLogger(PokerRoom.class);
	private UserData[] usersInTable;
	private UserData[] usersInGame;
	private List<RoundGame> rounds = new ArrayList<RoundGame>();
	private List<UserData> leaveRequests = new ArrayList<UserData>();
	private int tableSize;
	private SessionHandlerInt sessionHandler;
	private int dealerPosition;
	private RoundGame actualRound;
	private OrchestratorPipe orchestratorPipe;
	
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
		this.processLeaveRequests();
		if(!inGame && Utils.checkPlayers(usersInTable) >= 2) { // FIXME: change this, get from configuration, is in two only for test/dev purposes.
			// START GAME
			log.debug("START GAME");
			this.inGame = true;
			// start game:
			final StartGame startGame = new StartGame();
			// FIXME: put this value in the configuration file.
			startGame.startIn = 20; // initial time
			//final SessionHandlerInt _sessionHandler = sessionHandler;
			final Timer timer = new Timer("StartGameTimmer");
			TimerTask timeToStartGame = new TimerTask() {
				public void run() {
					startGame.startIn -= 5;
					if(startGame.startIn <= 0) {
						timer.cancel();
						realStartGame();
					} else {
						sessionHandler.sendToAll("/GameController/startGame", startGame);
						log.debug("Start game in: " + startGame.startIn);
					}
				}
			};
			timer.scheduleAtFixedRate(timeToStartGame, 5000L, 5000L);
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
		// TODO: ignore players sitted but without deposit in usersInGame:
		if(Utils.countUsersCanPlay(usersInTable) > 1) {			
			usersInGame = Utils.getNewArrayOfUsers(usersInTable);
			this.dealerPosition = Utils.getNextPositionOfPlayers(usersInGame, this.dealerPosition);
			rounds.add(0, actualRound);
			actualRound = new RoundGame(new Deck(), usersInGame, this.dealerPosition);
			if(actualRound.start()) {
				startRound();
			}
		} else {
			this.inGame = false;
			final StartGame startGame = new StartGame();
			startGame.startIn = -1; // initial time
			actualRound = null;
			sessionHandler.sendToAll("/GameController/startGame", startGame);
			Utils.getPlayersWithoutChips(usersInTable).forEach(user -> {
				sessionHandler.sendToSessID("GameController/deposit", user.sessID, new RequestDeposit());
			});
		}
	}

	public void dumpSnapshot(String sessID, Object objectID) {
		sessionHandler.sendToSessID("/GameController/snapshot", sessID, getSnapshot(actualRound, objectID));
	}
	
	private Snapshot getSnapshot(RoundGame round, Object objectID) {
		Snapshot snap = new Snapshot();
		snap.players = new ArrayList<SnapshotPlayer>();
		snap.myPosition = (Integer) objectID;
		snap.bigBlind = RoundGame.getBigBlind();
		snap.smallBlind = RoundGame.getSmallBlind();
		for(int i = 0; i<this.tableSize; i++) {
			if(usersInTable[i] != null) {
				SnapshotPlayer player = new SnapshotPlayer();
				player.chips = usersInTable[i].chips;
				player.nick = usersInTable[i].dataBlock.getNick_name();
				player.photo = usersInTable[i].dataBlock.getPhoto();
				player.inGame = false;
				if(round != null) {
					player.actualBet = round.getBetOf(i);
					player.showingCards = false;
					player.haveCards = round.haveCards(i);
					player.inGame = round.isInGame(i);
					// Me?
					if(i == snap.myPosition || snap.myPosition < 0) {
						player.showingCards = true;
						player.cards = round.getCards(i);
					}
					//player.showingCards?
					//player.cards = getCardsForUser with player.showingCards?
				}
				snap.players.add(player);
			} else {
				snap.players.add(null);
			}
		}
		snap.isDealing = false;
		snap.isInRest = false;
		if (round != null) {
			snap.isInRest = true;
			snap.dealerPosition = round.getDealerPosition();
			List<Long> pots = Utils.getPotValues(round.getPot());
			snap.pots = pots; // get actual pot
			Card[] cards = round.getCommunityCards();
			snap.communityCards = new ArrayList<SchemaCard>();
			for(int i = 0; i<cards.length; i++) {
				snap.communityCards.add(Utils.getSchemaFromCard(cards[i]));
			}
			snap.roundStep = round.getStep();
			// action?
			if(round.checkWaiting()) {
				snap.waitingFor = round.getWaitingActionFromPlayer();
				// is me?
				if(snap.myPosition == snap.waitingFor) {
					snap.betDecision = round.calcDecision();
				}
			}
		}
		return snap;
	}
	
	public void receivedMessage(SchemaGameProto schemaGameProto, String serializedMessage, String socketSessionID) {
		try {
			ObjectMapper om = new ObjectMapper();
			if(schemaGameProto.schema.equals("decisionInform")) {
				DecisionInform dI = om.readValue(serializedMessage, DecisionInform.class);
				// si es el jugador de la posición correcta:
				UserData uD = sessionHandler.getUserDataBySession(socketSessionID);
				boolean finishedRound = actualRound.processDecision(dI, uD);
				if (finishedRound) {
					startRound();
				}
			}
			if(schemaGameProto.schema.equals("SnapshotRequest")) {
				UserData uD = sessionHandler.getUserDataBySession(socketSessionID);
				SnapshotRequest sr = om.readValue(serializedMessage, SnapshotRequest.class);
				if(rounds.size() > sr.round) {					
					sessionHandler.sendToSessID("/GameController/snapshot", uD.sessID, getSnapshot(rounds.get(sr.round), -1));
					rounds.get(sr.round).resendWinners(uD.sessID);
				}
			}
			if(schemaGameProto.schema.equals("ChatMessage")) {
				UserData uD = sessionHandler.getUserDataBySession(socketSessionID);
				ChatMessage cm = om.readValue(serializedMessage, ChatMessage.class);
				// si el juego ya empezó
				if(actualRound != null) {
					boolean inRoom = false;
					// verificamos que esté entre los jugadores
					for(var user: usersInTable) {
						if(user != null && user.sessID.equals(socketSessionID)) {
							inRoom = true;
						}
					}
					if(inRoom) {
						cm.author = uD.dataBlock.getNick_name();
						sessionHandler.sendToAll("/GameController/chat", cm);
					}
				} else { // si el juego no empezó todos pueden hablar
					cm.author = uD.dataBlock.getNick_name();
					sessionHandler.sendToAll("/GameController/chat", cm);
				}
			}
			log.debug("Receive message from " + socketSessionID);
		} catch (JsonMappingException e) {
			log.error("Fail to process Schema:",e);
		} catch (JsonProcessingException e) {
			log.error("Fail to process Schema:",e);
		}
	}

	public void onNewPlayerSitdown(UserData player) {
		// TODO Auto-generated method stub
		log.debug("New Player: " + player.userID);
	}

	public void onDeposit(UserData player, long chipsDeposited) {
		// TODO Auto-generated method stub
		log.debug("New Deposit to: " + player.userID + " chips: " + chipsDeposited);
		for(int i = 0; i < usersInTable.length; i++) {
			if(usersInTable[i] != null && usersInTable[i].userID == player.userID) {
				DepositAnnouncement da = new DepositAnnouncement(i, chipsDeposited);
				sessionHandler.sendToAll("/GameController/depositAnnouncement", da);				
			}
		}
	}
	
	private void processLeaveRequests() {
		this.leaveRequests.forEach(lr -> {
			int ppos = Utils.getPlyerPosition(usersInGame, lr);
			usersInGame[ppos] = null;
			var ln = new LeaveNotify();
			ln.position = ppos;
			sessionHandler.sendToAll("/GameController/leaveNotify", ln);
			// enviar fichas al orchestrator.
		});
	}

	@Override
	public void onUserLeave(SchemaGameProto schemaGameProto, String serializedMessage, String socketSessionID) {
		if(schemaGameProto.schema.equals("leaveReq")) {
			this.leaveRequests.add(sessionHandler.getUserDataBySession(socketSessionID));
		}
	}

	@Override
	public void onNewOrchestratorPipe(OrchestratorPipe orchestratorPipe) {
		this.orchestratorPipe = orchestratorPipe;
	}
	
}
