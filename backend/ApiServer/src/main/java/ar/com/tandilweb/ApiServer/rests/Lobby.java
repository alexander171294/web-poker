package ar.com.tandilweb.ApiServer.rests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ar.com.tandilweb.ApiServer.dataTypesObjects.generic.ValidationException;
import ar.com.tandilweb.ApiServer.dataTypesObjects.lobby.ChallengeResponse;
import ar.com.tandilweb.ApiServer.dataTypesObjects.lobby.RoomResponse;
import ar.com.tandilweb.ApiServer.dataTypesObjects.lobby.RoomsResponse;
import ar.com.tandilweb.ApiServer.transport.LobbyAdapter;

@RestController
@RequestMapping("/lobby")
public class Lobby {
	
	@Autowired
	LobbyAdapter lobbyAdapter;
	
	@RequestMapping(path="/rooms", method=RequestMethod.GET)
	public ResponseEntity<RoomsResponse> getRooms() {
		RoomsResponse out = new RoomsResponse();
		try {
			out.rooms = lobbyAdapter.getRooms();
			out.operationSuccess = true;
			return new ResponseEntity<RoomsResponse>(out, HttpStatus.OK);
		} catch (Exception e) {
			out.operationSuccess = false;
			out.errorDescription = e.getMessage();
			out.errorCode = -1;
			return new ResponseEntity<RoomsResponse>(out, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(path="/rooms/gproto/{protocol}", method=RequestMethod.GET)
	public ResponseEntity<RoomsResponse> getRoomsByProtocol(@PathVariable("protocol") String protocolID) {
		RoomsResponse out = new RoomsResponse();
		try {
			if (protocolID == null || protocolID.length() <= 16) {
				throw new ValidationException(1, "You must send protocol");
			}
			if (!protocolID.startsWith("eppr/game-proto#")) {
				throw new ValidationException(2, "The protocol name is not EPPR based protocol");
			}
			out.rooms = lobbyAdapter.getRoomsByProtocol(protocolID);
			out.operationSuccess = true;
			return new ResponseEntity<RoomsResponse>(out, HttpStatus.OK);
		} catch (ValidationException vE) {
			out.operationSuccess = false;
			out.errorCode = vE.getIdECode();
			out.errorDescription = vE.getMessage();
			return new ResponseEntity<RoomsResponse>(out, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			out.operationSuccess = false;
			out.errorDescription = e.getMessage();
			out.errorCode = -1;
			return new ResponseEntity<RoomsResponse>(out, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(path="/rooms/{id}", method=RequestMethod.GET)
	public ResponseEntity<RoomResponse> getRoomsByID(@PathVariable("id") long roomID) {
		try {
			if (roomID <= 0) {
				throw new ValidationException(1, "Invalid RoomID");
			}
			RoomResponse out = lobbyAdapter.getRoomByID(roomID);
			return new ResponseEntity<RoomResponse>(out, HttpStatus.OK);
		} catch (ValidationException vE) {
			RoomResponse out = new RoomResponse();
			out.operationSuccess = false;
			out.errorCode = vE.getIdECode();
			out.errorDescription = vE.getMessage();
			return new ResponseEntity<RoomResponse>(out, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			RoomResponse out = new RoomResponse();
			out.operationSuccess = false;
			out.errorDescription = e.getMessage();
			out.errorCode = -1;
			return new ResponseEntity<RoomResponse>(out, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(path="/rooms/{id}", method=RequestMethod.POST)
	public ResponseEntity<ChallengeResponse> challengeRooms(@PathVariable("id") int roomID) {
		try {
			if (roomID <= 0) {
				throw new ValidationException(1, "Invalid RoomID");
			}
			ChallengeResponse out = lobbyAdapter.challengeRoomByID(roomID);
			return new ResponseEntity<ChallengeResponse>(out, HttpStatus.OK);
		} catch (ValidationException vE) {
			ChallengeResponse out = new ChallengeResponse();
			out.operationSuccess = false;
			out.errorCode = vE.getIdECode();
			out.errorDescription = vE.getMessage();
			return new ResponseEntity<ChallengeResponse>(out, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			ChallengeResponse out = new ChallengeResponse();
			out.operationSuccess = false;
			out.errorDescription = e.getMessage();
			out.errorCode = -1;
			return new ResponseEntity<ChallengeResponse>(out, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
