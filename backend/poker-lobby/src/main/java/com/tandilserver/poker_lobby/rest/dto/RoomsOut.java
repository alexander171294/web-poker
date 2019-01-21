package com.tandilserver.poker_lobby.rest.dto;

import com.tandilserver.poker_lobby.dataBase.customTypes.LimitTypes;
import com.tandilserver.poker_lobby.dataBase.customTypes.ServerTypes;

public class RoomsOut {
	private long id_server;
	private String ip;
	private int port;
	private String server_name;
	private int players;
	private long blind;
	private long min_bet;
	private ServerTypes server_type;
	private LimitTypes limit_bet;
	private boolean official_server;
	
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
	public boolean isOfficialServer() {
		return official_server;
	}
	public void setOfficialServer(boolean officialServer) {
		this.official_server = officialServer;
	}
}
