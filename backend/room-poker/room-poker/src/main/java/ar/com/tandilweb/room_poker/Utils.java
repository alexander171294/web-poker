package ar.com.tandilweb.room_poker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	
	public static int getRandomPositionOfTakens(UserData[] usersInTable) {
		List<Integer> takenSpaces = new ArrayList<Integer>();
		for(int i = 0; i < usersInTable.length; i++) {
			if (usersInTable[i] != null) {
				takenSpaces.add(i);
			}
		}
		Random rand = new Random();
		return takenSpaces.get(rand.nextInt(takenSpaces.size()));
	}
	
	public static int getNextPositionOfPlayers(UserData[] usersInGame, int actualPosition) {
		return 3; // FIXME
	}
	
	public static UserData[] getNewArrayOfUsers(UserData[] usersInTable) {
		UserData[] usersInGame = new UserData[usersInTable.length]; 
		for(int i = 0; i < usersInTable.length; i++) {
			usersInGame[i] = usersInTable[i];
		}
		return usersInGame;
	}

}
