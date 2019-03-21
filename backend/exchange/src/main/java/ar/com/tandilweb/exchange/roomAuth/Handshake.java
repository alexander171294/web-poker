package ar.com.tandilweb.exchange.roomAuth;

import ar.com.tandilweb.exchange.RoomAuthSchema;

public class Handshake extends RoomAuthSchema {
	
	public String name;
	public long serverID;
	public String accessPassword;
	public String securityToken;
	public String gproto;
	public int maxPlayers;
	public String description;
	public int minCoinsForAccess;
	
	public Handshake() {
		super("handshake");
	}

}
