package com.tandilserver.poker_intercom.handshake;

import com.tandilserver.poker_intercom.customTypes.LimitTypes;
import com.tandilserver.poker_intercom.customTypes.ServerTypes;

public class ServerInfo {

	public String ip;
	public int port;
	public String server_name;
	public int players;
	public long blind;
	public long min_bet;
	public ServerTypes server_type;
	public LimitTypes limit_bet;
	public boolean officialServer;
	
}
