package com.tandilserver.poker_lobby.dataBase.domain;

import com.tandilserver.poker_lobby.dataBase.customTypes.LimitTypes;
import com.tandilserver.poker_lobby.dataBase.customTypes.ServerTypes;

public class Rooms {
	public String ip;
	public int port;
	public String server_name;
	public int players;
	public long blind;
	public long min_bet;
	public ServerTypes server_type;
	public LimitTypes limit_bet;
}
