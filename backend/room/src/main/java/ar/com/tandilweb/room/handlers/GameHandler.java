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
		// request define position (if is needed) or Reject fully?
		// request deposit (if is needed)
		// Announcement
		// FIXME: check start game?
	}
	
}
