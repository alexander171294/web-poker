package ar.com.tandilweb.room_int.handlers;

import java.util.List;

import ar.com.tandilweb.room_int.handlers.dto.UserData;

/**
 * @author Alexander Eberle
 */
public interface SessionHandlerInt {
	
	
	/**
	 * Function for get UserData objet from a Session ID
	 * @param session
	 * @return
	 */
	UserData getUserDataBySession(String session);
	
	/**
	 * Check if is active session for user.
	 * @param userID
	 * @return
	 */
	boolean isActiveSessionForUser(long userID);
	
	/**
	 * Get UserData object from UserID
	 * Warning: this function iterate over users to find Session ID and then call to getUserDataBySession
	 * @param userID
	 * @return
	 */
	UserData getUserDataFromActiveSessionForUser(long userID);
	
	/**
	 * Get other sessions than meSession for userID.
	 * @param meSession
	 * @param userID
	 * @return
	 */
	List<UserData> getAnotherSessions(String meSession, long userID);
	
	/**
	 * Get the principal active session id for user.
	 * @param userID
	 * @return
	 */
	String getActiveSessionForUser(long userID);
	
	/**
	 * Send payload to a userID in "direction".
	 * Warning: this function iterate over users to find Session ID and then call to sendToSessID
	 * @param direction
	 * @param userID
	 * @param payload
	 */
	void sendToUserID(String direction, long userID, Object payload);
	
	/**
	 * Send a payload to a sessionID in "direction"
	 * @param direction
	 * @param sessionID
	 * @param payload
	 */
	void sendToSessID(String direction, String sessionID, Object payload);
	
	/**
	 * Send a payload to all sessions connected to server in "direction"
	 * @param direction
	 * @param payload
	 */
	void sendToAll(String direction, Object payload);
}
