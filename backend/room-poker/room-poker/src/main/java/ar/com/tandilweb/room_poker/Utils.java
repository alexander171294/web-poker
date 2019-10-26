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
		List<Integer> takenSpaces = getPlayers(usersInTable);
		Random rand = new Random();
		return takenSpaces.get(rand.nextInt(takenSpaces.size()));
	}
	
	public static int getNextPositionOfPlayers(UserData[] usersInGame, int actualPosition) {
		for(int i = actualPosition+1; i < usersInGame.length; i++) {
			if(usersInGame[i] != null) {
				return i;
			}
		}
		for(int i = 0; i < actualPosition; i++) {
			if(usersInGame[i] != null) {
				return i;
			}
		}
		return actualPosition; // wtf?, this only can be if the room has configured the minimum on 1 player.
	}
	
	public static UserData[] getNewArrayOfUsers(UserData[] usersInTable) {
		UserData[] usersInGame = new UserData[usersInTable.length]; 
		for(int i = 0; i < usersInTable.length; i++) {
			usersInGame[i] = usersInTable[i];
		}
		return usersInGame;
	}
	
	public static List<Integer> getPlayers(UserData[] usersInTable) {
		List<Integer> players = new ArrayList<Integer>();
		for(int i = 0; i < usersInTable.length; i++) {
			if (usersInTable[i] != null) {
				players.add(i);
			}
		}
		return players;
	}
	
	public static List<Integer> getPlayersFromPosition(UserData[] usersInTable, int startPosition) {
		List<Integer> players = new ArrayList<Integer>();
		for(int i = startPosition + 1; i < usersInTable.length; i++) {
			if (usersInTable[i] != null) {
				players.add(i);
			}
		}
		for(int i = 0; i <= startPosition; i++) {
			if (usersInTable[i] != null) {
				players.add(i);
			}
		}
		return players;
	}

}