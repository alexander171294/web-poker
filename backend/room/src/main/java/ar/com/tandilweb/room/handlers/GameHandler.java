package ar.com.tandilweb.room.handlers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.com.tandilweb.room.protocols.EpprGameProto;
import ar.com.tandilweb.room.protocols.EpprRoomAuth;
import ar.com.tandilweb.room_int.GameCtrlInt;
import ar.com.tandilweb.room_int.handlers.dto.UserData;

@Service
public class GameHandler {
	
	public static Logger logger = LoggerFactory.getLogger(GameHandler.class);
	
	@Autowired
	private SessionHandler sessionHandler;
	
	@Autowired
	private EpprGameProto gameProtocol;
	
	@Autowired
	private EpprRoomAuth roomAuthProtocol;
	
	@Autowired
	private GameCtrlInt gameController;
	
	private UserData[] usersInTable;
	private List<UserData> spectators = new ArrayList<UserData>();
	
	@Value("${act.room.RoomAuth.maxPlayers}")
	private int maxPlayers;
	
	@PostConstruct
    public void init() {
		usersInTable = new UserData[maxPlayers];
		gameController.setUsersInTableRef(usersInTable, sessionHandler);
		logger.debug("Started pool of users with size: "+maxPlayers);
	 }
	
	public void ingressFlow(UserData userData) {
		gameController.dumpSnapshot(userData.sessID);
		logger.debug("Ingressed user", userData);
		// check old position 
		List<Integer> freeSpaces = new ArrayList<Integer>();
		for(int i = 0; i<maxPlayers; i++) {
			if (usersInTable[i] != null) {
				if(usersInTable[i].userID == userData.userID) {
					this.ingressSchema(i, userData);
					return;
				}
			} else {
				freeSpaces.add(i);
			}
		}
		// DEFINEPOSITION SCHEMA.
		if(freeSpaces.size() > 0) {
			this.definePosition(userData.sessID, freeSpaces);
			// DEPOSIT SCHEMA -- see documentation of eppr
			sessionHandler.sendToSessID("GameController/deposit", userData.sessID, roomAuthProtocol.getDepositSchema());
		} else {
			for(UserData spectator: spectators) {
				if(spectator.userID == userData.userID) {
					spectators.remove(spectator);
				}
			}
			spectators.add(userData);
			sessionHandler.sendToSessID("GameController/rejectFullyfied", userData.sessID, this.gameProtocol.getRejectFullyfiedSchema());
		}
	}
	
	private void definePosition(String sessID, List<Integer> freeSpaces) {
		sessionHandler.sendToSessID("GameController/definePosition", sessID, this.gameProtocol.getDefinePositionSchema(freeSpaces));
	}
	
	private void definePosition(String sessID) {
		List<Integer> freeSpaces = new ArrayList<Integer>();
		for(int i = 0; i<maxPlayers; i++) {
			if (usersInTable[i] == null) {
				freeSpaces.add(i);
			}
		}
		if(freeSpaces.size() > 0) {
			definePosition(sessID, freeSpaces);
		} else {
			sessionHandler.sendToSessID("GameController/rejectFullyfied", sessID, this.gameProtocol.getRejectFullyfiedSchema());
		}
	}
	
	public void sitFlow(int position, UserData userData) {
		if(position >= maxPlayers || position < 0 || usersInTable[position] != null) {
			List<Integer> freeSpaces = new ArrayList<Integer>();
			for(int i = 0; i<maxPlayers; i++) {
				if (usersInTable[i] == null) {
					freeSpaces.add(i);
				}
			}
			if(freeSpaces.size() > 0) {
				sessionHandler.sendToSessID("GameController/rejectedPosition", userData.sessID, gameProtocol.getRejectedPositionSchema(freeSpaces));
			} else {
				sessionHandler.sendToSessID("GameController/rejectFullyfied", userData.sessID, this.gameProtocol.getRejectFullyfiedSchema());
			}
		} else {
			// FIXME: check if is in other position and remove.
			this.gameController.onNewPlayerSitdown(userData);
			usersInTable[position] = userData;
			this.ingressSchema(position, userData);
		}
	}
	
	public void ingressSchema(int position, UserData userData) {
		// INGRESS SCHEMA
		if(userData.chips == 0) {
			sessionHandler.sendToSessID("GameController/deposit", userData.sessID, roomAuthProtocol.getDepositSchema());
			return;
		}
		sessionHandler.sendToSessID("GameController/ingress", userData.sessID, gameProtocol.getIngressSchema(userData, position));
		// Announcement
		sessionHandler.sendToAll("/GameController/announcement", gameProtocol.getAnnouncementSchema(userData, position));
		gameController.checkStartGame();
	}
	
	public int getPositionOfUser(long userId) {
		for(int i = 0; i<maxPlayers; i++) {
			if(usersInTable[i] != null && usersInTable[i].userID == userId) {
				return i;
			}
		}
		return -1;
	}
	
	public void addChipsToUser(long userID, long chips, long accountChips) {
		boolean done = false;
		UserData uD = null;
		for(int i = 0; i<maxPlayers; i++) {
			if(usersInTable[i] != null && usersInTable[i].userID == userID) {
				usersInTable[i].chips += chips;
				usersInTable[i].dataBlock.setChips(accountChips);
				gameController.onDeposit(usersInTable[i], chips);
				gameController.checkStartGame();
				uD = usersInTable[i];
				done = true;
			}
		}
		if(!done) {
			// not in table.
			uD = sessionHandler.getUserDataFromActiveSessionForUser(userID);
			if(uD != null) {
				uD.chips = chips;
				gameController.onDeposit(uD, chips);
				definePosition(sessionHandler.getActiveSessionForUser(userID));
			} else {
				// TODO: refound chips.
				logger.error("Not user in memory for deposit: UID: "+userID+" Chips: "+chips+" AccountChips: "+accountChips);
			}
		}
	}
	
	@Scheduled(fixedRate = 60000)
	public void scheduleRoomStatus() throws JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
		logger.info("[Room Monitor] Users in table: " + om.writeValueAsString(usersInTable));
	}
}
