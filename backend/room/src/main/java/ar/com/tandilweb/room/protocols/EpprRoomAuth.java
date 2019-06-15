package ar.com.tandilweb.room.protocols;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ar.com.tandilweb.exchange.roomAuth.Handshake;
import ar.com.tandilweb.exchange.roomAuth.SignupData;

@Component
public class EpprRoomAuth {
	
	public static Logger logger = LoggerFactory.getLogger(EpprRoomAuth.class);
	
	@Value("${act.room.RoomAuth.name}")
	private String name;
	
	@Value("${act.room.RoomAuth.accessPassword}")
	private String accessPassword;
	
	@Value("${act.room.RoomAuth.securityToken}")
	private String securityToken;
	
	@Value("${act.room.RoomAuth.maxPlayers}")
	private int maxPlayers;
	
	@Value("${act.room.RoomAuth.description}")
	private String description;
	
	@Value("${act.room.RoomAuth.minCoinsForAccess}")
	private int minCoinsForAccess;
	
	@Value("${act.room.RoomAuth.recoveryEmail}")
	private String recoveryEmail;
	
	@Value("${act.room.RoomAuth.serverPublicIP}")
	private String serverPublicIP;
	
	// TODO: finish logic of this (see eppr/room-auth::handshake) 
	public Handshake getHandshakeSchema() {
		Handshake handshake = new Handshake();
		handshake.name = name;
		//handshake.serverID = 0;
		handshake.accessPassword = accessPassword;
		handshake.securityToken = securityToken;
		handshake.gproto = "eppr/game-proto#texasholdem";
		handshake.maxPlayers = maxPlayers;
		handshake.description = description;
		handshake.minCoinsForAccess = minCoinsForAccess;
		handshake.serverPublicAP = (serverPublicIP == null) ? getMyLocalIP() : serverPublicIP;
		return handshake;
	}
	
	public SignupData getSignupSchema() {
		SignupData signupData = new SignupData();
		signupData.recoveryEmail = recoveryEmail;
		return signupData;
	}
	
	private String getMyLocalIP() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException ukHostException) {
			logger.error("Unknown Host Exception", ukHostException);
			return null;
		}
	}

}
