package ar.com.tandilweb.room.handlers;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ar.com.tandilweb.room.handlers.dto.UserData;

@Service
public class GameHandler {
	
	public static Logger logger = LoggerFactory.getLogger(GameHandler.class);
	
	private UserData[] usersInTable;
	
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
		int freeSpaces = 0;
		for(int i = 0; i<maxPlayers; i++) {
			if (usersInTable[i] != null) {
				if(usersInTable[i].userID == userData.userID) {
					this.ingressSchema(i, userData);
				} else {
					// DEFINEPOSITION SCHEMA
					// DEPOSIT SCHEMA -- see documentation of eppr
					// INGRESS SCHEMA
				}
			} else {
				freeSpaces++;
			}
		}
	}
	
	public void ingressSchema(int position, UserData userData) {
		// INGRESS SCHEMA
		// Announcement
		// FIXME: check start game?
	}
	
}
