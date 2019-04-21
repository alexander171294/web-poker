package ar.com.tandilweb.orchestrator.adapters;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.tandilweb.exchange.Schema;
import ar.com.tandilweb.exchange.roomAuth.Busy;
import ar.com.tandilweb.exchange.roomAuth.Handshake;
import ar.com.tandilweb.exchange.roomAuth.Rejected;
import ar.com.tandilweb.exchange.roomAuth.Retry;
import ar.com.tandilweb.exchange.roomAuth.Signup;
import ar.com.tandilweb.exchange.roomAuth.SignupData;
import ar.com.tandilweb.exchange.roomAuth.SignupResponse;
import ar.com.tandilweb.exchange.roomAuth.TokenUpdate;
import ar.com.tandilweb.orchestrator.persistence.domain.Rooms;
import ar.com.tandilweb.orchestrator.persistence.repository.RoomsRepository;
import ar.com.tandilweb.orchestrator.protocols.EpprRoomAuth;

@Service
public class RoomAuthService {
	
	protected static Logger logger = LoggerFactory.getLogger(RoomAuthService.class);
	
	@Autowired
	private RoomsRepository roomsRepository;
	
	@Autowired
	private EpprRoomAuth roomAuthProto;
	
	public Schema handshakeValidate(Handshake handshake) {
		if(handshake.serverID > 0) {
			// limit exceeded
			Rooms room = roomsRepository.findById(handshake.serverID);
			if(room.getBadLogins() > 3) { // FIXME: get this hardcoded value from properties.
				// TODO: send email warning of block.
				return new Rejected();
			}
			if(room.getSecurityToken().equals(handshake.securityToken) && room.getAccessPassword().equals(handshake.accessPassword)) {
				TokenUpdate tU = roomAuthProto.getTokenUpdateSchema();
				tU.securityToken = UUID.randomUUID().toString();
				room.setSecurityToken(tU.securityToken);
				roomsRepository.update(room);
				return tU;
			}
			// security fail
			room.setBadLogins(room.getBadLogins() + 1);
			roomsRepository.update(room);
			return new Retry();
		}
		// TODO: check limit of IP in new server.
		// return new Exceeded();
		// if not exceeded the limit:
		return new Signup();
	}
	
	public Schema signupDataValidate(SignupData signupData, Handshake handshake) {
		// TODO: check limit of IP in new server.
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
		room = roomsRepository.create(room);
		if(room != null) {
			// registered ok:
			SignupResponse sr = new SignupResponse();
			sr.securityToken = room.getSecurityToken();
			sr.serverID = room.getId_room();
			return sr;
		} else {
			// fail register
			// FIXME: add ip of request to log.-
			logger.error("Room Signup fatal error", handshake);
			return new Busy();
		}
	}

}
