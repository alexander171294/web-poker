package ar.com.tandilweb.room_int;

import ar.com.tandilweb.exchange.gameProtocol.SchemaGameProto;
import ar.com.tandilweb.room_int.handlers.SessionHandlerInt;
import ar.com.tandilweb.room_int.handlers.dto.UserData;

public interface GameCtrlInt {
	
	void setUsersInTableRef(UserData[] usersInTable, SessionHandlerInt sessionHandler);
	
	void checkStartGame();
	
	void dumpSnapshot();
	
	void receivedMessage(SchemaGameProto schemaGameProto, String serializedMessage, String socketSessionID);
	
	void onNewPlayerSitdown(UserData player);
	
	void onDeposit(UserData player, long chipsDeposited);
	
	// TODO: add onLeave
	//void onUserLeave(UserData player);
	
	// TODO: add unexpected disconnect
	//void onUserUnexpectedDisconnect(UserData player);

}
