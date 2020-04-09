package ar.com.tandilweb.ApiServer.dataTypesObjects.lobby;

import ar.com.tandilweb.ApiServer.dataTypesObjects.generic.GeneralResponse;

public class RoomResponse extends GeneralResponse {
	
	public long id_room;
	public String server_ip;
	public String name;
	public String gproto;
	public String description;
	public int max_players;
	public int minCoinForAccess;
	public long players;
	public boolean isOfficial;

}
