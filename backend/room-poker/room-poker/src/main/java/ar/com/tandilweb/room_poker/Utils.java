package ar.com.tandilweb.room_poker;

import ar.com.tandilweb.room_int.handlers.dto.UserData;

public class Utils {
	
	public static int checkPlayers(UserData[] usersInTable) {
		int countUsers = 0;
		for(int i = 0; i < usersInTable.length; i++) {
			if (usersInTable[i] != null) {
				countUsers++;
			}
		}
		return countUsers;
	}

}
