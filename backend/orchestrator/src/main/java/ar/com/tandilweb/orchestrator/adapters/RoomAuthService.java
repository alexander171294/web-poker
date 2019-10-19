package ar.com.tandilweb.orchestrator.adapters;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.tandilweb.exchange.roomAuth.Busy;
import ar.com.tandilweb.exchange.roomAuth.Handshake;
import ar.com.tandilweb.exchange.roomAuth.Rejected;
import ar.com.tandilweb.exchange.roomAuth.Retry;
import ar.com.tandilweb.exchange.roomAuth.Signup;
import ar.com.tandilweb.exchange.roomAuth.SignupData;
import ar.com.tandilweb.exchange.roomAuth.SignupResponse;
import ar.com.tandilweb.exchange.roomAuth.TokenUpdate;
import ar.com.tandilweb.orchestrator.handlers.LoginResponse;
import ar.com.tandilweb.orchestrator.persistence.repository.RoomsRepository;
import ar.com.tandilweb.orchestrator.protocols.EpprRoomAuth;
import ar.com.tandilweb.persistence.domain.Rooms;

@Service
public class RoomAuthService {
	
	protected static Logger logger = LoggerFactory.getLogger(RoomAuthService.class);
	
	@Autowired
	private RoomsRepository roomsRepository;
	
	@Autowired
	private EpprRoomAuth roomAuthProto;
	
	public LoginResponse handshakeValidate(Handshake handshake) {
		LoginResponse out = new LoginResponse();
		out.logged = false;
		if(handshake.serverID > 0) {
			// limit exceeded
			Rooms room = roomsRepository.findById(handshake.serverID);
			if(room != null) {
				if(room.getBadLogins() > 3) { // FIXME: get this hardcoded value from properties.
					// TODO: send email warning of block.
					out.response = new Rejected();
					return out;
				}
				if(room.getSecurityToken().equals(handshake.securityToken) && room.getAccessPassword().equals(handshake.accessPassword)) {
					TokenUpdate tU = roomAuthProto.getTokenUpdateSchema();
					tU.securityToken = UUID.randomUUID().toString();
					room.setSecurityToken(tU.securityToken);
					room.setServer_ip(handshake.serverPublicAP);
					roomsRepository.update(room);
					out.response = tU;
					out.logged = true;
					return out;
				}
				// security fail
				room.setBadLogins(room.getBadLogins() + 1);
				roomsRepository.update(room);
				out.response = new Retry();
				return out;
			}
		}
		// TODO: check limit of IP in new server.
		// return new Exceeded();
		// if not exceeded the limit:
		out.response = new Signup();
		return out;
	}
	
	public LoginResponse signupDataValidate(SignupData signupData, Handshake handshake) {
		// TODO: check limit of IP in new server.
		LoginResponse out = new LoginResponse();
		Rooms room = new Rooms();
		room.setAccessPassword(handshake.accessPassword);
		room.setBadLogins(0);
		room.setDescription(handshake.description);
		room.setGproto(handshake.gproto);
		room.setMax_players(handshake.maxPlayers);
		room.setMinCoinForAccess(handshake.minCoinsForAccess);
		room.setName(handshake.name);
		room.setNowConnected(false);
		room.setOfficial(false);
		String token = UUID.randomUUID().toString();
		room.setSecurityToken(token);
		room.setRecoveryEmail(signupData.recoveryEmail);
		room.setServer_ip(handshake.serverPublicAP);
		room = roomsRepository.create(room);
		if(room != null) {
			// registered ok:
			SignupResponse sr = new SignupResponse();
			sr.securityToken = room.getSecurityToken();
			sr.serverID = room.getId_room();
			out.response = sr;
			out.logged = true;
			return out;
		}
		// fail register
		// FIXME: add ip of request to log.-
		logger.error("Room Signup fatal error", handshake);
		out.response = new Busy();
		out.logged = false;
		return out;
	}

}
