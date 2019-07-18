package ar.com.tandilweb.room.protocols;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

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
	
	public Handshake getHandshakeSchema() {
		Handshake handshake = new Handshake();
		handshake.name = name;
		handshake.accessPassword = accessPassword;
		handshake.securityToken = securityToken;
		handshake.maxPlayers = maxPlayers;
		handshake.description = description;
		handshake.minCoinsForAccess = minCoinsForAccess;
		handshake.gproto = "eppr/game-proto#texasholdem";
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
