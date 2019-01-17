package com.tandilserver.poker_lobby.dataBase.domain;

import com.tandilserver.poker_lobby.dataBase.customTypes.LimitTypes;
import com.tandilserver.poker_lobby.dataBase.customTypes.ServerTypes;

public class Rooms {
	
	private long id_server;
	private String server_identity_hash;
	private String ip;
	private int port;
	private String server_name;
	private int players;
	private long blind;
	private long min_bet;
	private ServerTypes server_type;
	private LimitTypes limit_bet;
	private boolean official_server;
	
	private boolean newItem;

	public Rooms() {
		
	}
	
	public Rooms(Long id_server, String ip, int port, String server_name, int players, long blind, long min_bet, ServerTypes server_type, LimitTypes limit_bet, String server_identity_hash, boolean official_server) {
		this.id_server = id_server;
		this.ip = ip;
		this.port = port;
		this.server_name = server_name;
		this.players = players;
		this.blind = blind;
		this.min_bet = min_bet;
		this.server_type = server_type;
		this.limit_bet = limit_bet;
		this.server_identity_hash = server_identity_hash;
		this.official_server = official_server;
	}
	
	public long getId_server() {
		return id_server;
	}
	public void setId_server(long id_server) {
		this.id_server = id_server;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getServer_name() {
		return server_name;
	}
	public void setServer_name(String server_name) {
		this.server_name = server_name;
	}
	public int getPlayers() {
		return players;
	}
	public void setPlayers(int players) {
		this.players = players;
	}
	public long getBlind() {
		return blind;
	}
	public void setBlind(long blind) {
		this.blind = blind;
	}
	public long getMin_bet() {
		return min_bet;
	}
	public void setMin_bet(long min_bet) {
		this.min_bet = min_bet;
	}
	public ServerTypes getServer_type() {
		return server_type;
	}
	public void setServer_type(ServerTypes server_type) {
		this.server_type = server_type;
	}
	public void setServer_type(int server_type) {
		this.server_type = ServerTypes.values()[server_type];
	}
	public LimitTypes getLimit_bet() {
		return limit_bet;
	}
	public void setLimit_bet(LimitTypes limit_bet) {
		this.limit_bet = limit_bet;
	}
	public void setLimit_bet(int limit_bet) {
		this.limit_bet = LimitTypes.values()[limit_bet];
	}
	public String getServerIdentityHash() {
		return server_identity_hash;
	}
	public void setServerIdentityHash(String serverIdentityHash) {
		this.server_identity_hash = serverIdentityHash;
	}

	public boolean isNewItem() {
		return newItem;
	}

	public void setNewItem(boolean newItem) {
		this.newItem = newItem;
	}

	public boolean isOfficialServer() {
		return official_server;
	}

	public void setOfficialServer(boolean officialServer) {
		this.official_server = officialServer;
	}
	
	
}
