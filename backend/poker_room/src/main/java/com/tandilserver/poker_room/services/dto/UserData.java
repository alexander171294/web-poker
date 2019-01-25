package com.tandilserver.poker_room.services.dto;

import java.util.Date;

public class UserData {
	public long id_usuario;
	public String nick;
	public Date signup_date;
	public long coins_registered;
	public long coins_in_game;
	public String signature;
	public String messageRouterSessionUUID;
	public String socketSessionUUID;
	public boolean verified;
	public boolean playing;
}
