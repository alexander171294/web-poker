package ar.com.tandilweb.room.handlers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ar.com.tandilweb.room.handlers.dto.UserData;
import ar.com.tandilweb.room.protocols.EpprGameProto;
import ar.com.tandilweb.room.protocols.EpprRoomAuth;

@Service
public class GameHandler {
	
	public static Logger logger = LoggerFactory.getLogger(GameHandler.class);
	
	@Autowired
	private SessionHandler sessionHandler;
	
	@Autowired
	private EpprGameProto gameProtocol;
	
	@Autowired
	private EpprRoomAuth roomAuthProtocol;
	
	private UserData[] usersInTable;
	private List<UserData> spectators = new ArrayList<UserData>();
	
	@Value("${act.room.RoomAuth.maxPlayers}")
	private int maxPlayers;
	
	@PostConstruct
    public void init() {
		usersInTable = new UserData[maxPlayers];
		logger.debug("Started pool of users with size: "+maxPlayers);
	 }
	
	public void ingressFlow(UserData userData) {
		// TODO: dump table snapshot (including table size)
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
			sessionHandler.sendToSessID("GameController/definePosition", userData.sessID, this.gameProtocol.getDefinePositionSchema(freeSpaces));
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
	
	public void sitFlow(int position, UserData userData) {
		if(position >= maxPlayers || usersInTable[position] != null) {
			List<Integer> freeSpaces = new ArrayList<Integer>();
			for(int i = 0; i<maxPlayers; i++) {
				if (usersInTable[i] == null) {
					freeSpaces.add(i);
				}
			}
			if(freeSpaces.size() > 0) {
				sessionHandler.sendToAll("GameController/rejectedPosition", gameProtocol.getRejectedPositionSchema(freeSpaces));
			} else {
				sessionHandler.sendToSessID("GameController/rejectFullyfied", userData.sessID, this.gameProtocol.getRejectFullyfiedSchema());
			}
		} else {
			usersInTable[position] = userData;
			if(userData.chips > 0) {
				this.ingressSchema(position, userData);
			}
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
		sessionHandler.sendToAll("GameController/announcement", gameProtocol.getAnnouncementSchema(userData, position));
		// FIXME: check start game?
	}
	
	public int getPositionOfUser(long userId) {
		for(int i = 0; i<maxPlayers; i++) {
			if(usersInTable[i] != null && usersInTable[i].userID == userId) {
				return i;
			}
		}
		return -1;
	}
}
