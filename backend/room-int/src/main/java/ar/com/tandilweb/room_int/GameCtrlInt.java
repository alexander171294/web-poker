package ar.com.tandilweb.room_int;

import ar.com.tandilweb.exchange.gameProtocol.SchemaGameProto;
import ar.com.tandilweb.room_int.handlers.SessionHandlerInt;
import ar.com.tandilweb.room_int.handlers.dto.UserData;

public interface GameCtrlInt {
	
	void setUsersInTableRef(UserData[] usersInTable);
	
	void checkStartGame();
	
	void dumpSnapshot();
	
	void receivedMessage(SchemaGameProto message, String socketSessionID, SessionHandlerInt sessionHandler);

}
