package ar.com.tandilweb.room.protocols;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ar.com.tandilweb.exchange.roomAuth.Handshake;
import ar.com.tandilweb.exchange.roomAuth.SignupData;

@Component
public class EpprRoomAuth {
	
	@Value("${ar.com.tandilweb.room.protocols.EpprRoomAuth.name}")
	private String name;
	
	@Value("${ar.com.tandilweb.room.protocols.EpprRoomAuth.accessPassword}")
	private String accessPassword;
	
	@Value("${ar.com.tandilweb.room.protocols.EpprRoomAuth.securityToken}")
	private String securityToken;
	
	@Value("${ar.com.tandilweb.room.protocols.EpprRoomAuth.maxPlayers}")
	private int maxPlayers;
	
	@Value("${ar.com.tandilweb.room.protocols.EpprRoomAuth.description}")
	private String description;
	
	@Value("${ar.com.tandilweb.room.protocols.EpprRoomAuth.minCoinsForAccess}")
	private int minCoinsForAccess;
	
	@Value("${ar.com.tandilweb.room.protocols.EpprRoomAuth.recoveryEmail}")
	private String recoveryEmail;
	
	@Value("${ar.com.tandilweb.room.protocols.EpprRoomAuth.serverPublicIP}")
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
		handshake.serverPublicAP = serverPublicIP;
		return handshake;
	}
	
	public SignupData getSignupSchema() {
		SignupData signupData = new SignupData();
		signupData.recoveryEmail = recoveryEmail;
		return signupData;
	}

}
