package ar.com.tandilweb.room_int;

import ar.com.tandilweb.exchange.gameProtocol.SchemaGameProto;
import ar.com.tandilweb.room_int.handlers.SessionHandlerInt;
import ar.com.tandilweb.room_int.handlers.dto.UserData;

public interface GameCtrlInt {
	
	void setUsersInTableRef(UserData[] usersInTable, SessionHandlerInt sessionHandler);
	
	void checkStartGame();
	
	void dumpSnapshot(String sessID, Object objectID);
	
	void receivedMessage(SchemaGameProto schemaGameProto, String serializedMessage, String socketSessionID);
	
	void onNewPlayerSitdown(UserData player);
	
	void onDeposit(UserData player, long chipsDeposited);
	
	void onUserLeave(SchemaGameProto schemaGameProto, String serializedMessage, String socketSessionID);
	
	void onNewOrchestratorPipe(OrchestratorPipe orchestratorPipe);
	
	// TODO: add unexpected disconnect
	//void onUserUnexpectedDisconnect(UserData player);

}
