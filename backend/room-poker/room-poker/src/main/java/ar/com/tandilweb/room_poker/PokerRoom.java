package ar.com.tandilweb.room_poker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.tandilweb.exchange.gameProtocol.SchemaGameProto;
import ar.com.tandilweb.room_int.GameCtrlInt;
import ar.com.tandilweb.room_int.handlers.SessionHandlerInt;
import ar.com.tandilweb.room_int.handlers.dto.UserData;

public class PokerRoom implements GameCtrlInt {
	
	private static final Logger log = LoggerFactory.getLogger(PokerRoom.class);

	public void setUsersInTableRef(UserData[] usersInTable) {
		// TODO Auto-generated method stub
		log.debug("Set Users In Table Ref");
	}

	public void checkStartGame() {
		// TODO Auto-generated method stub
		log.debug("Check Start Game");
	}

	public void dumpSnapshot() {
		// TODO Auto-generated method stub
		log.debug("Dump Snapshot");
	}

	public void receivedMessage(SchemaGameProto message, String socketSessionID, SessionHandlerInt sessionHandler) {
		// TODO Auto-generated method stub
		log.debug("Receive message from " + socketSessionID);
	}

	public void onNewPlayerSitdown(UserData player) {
		// TODO Auto-generated method stub
		log.debug("New Player: " + player.userID);
	}

}
