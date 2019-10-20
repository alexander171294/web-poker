package ar.com.tandilweb.ApiServer.rests;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.com.tandilweb.ApiServer.dataTypesObjects.generic.ValidationException;
import ar.com.tandilweb.ApiServer.dataTypesObjects.lobby.ChallengeResponse;
import ar.com.tandilweb.ApiServer.transport.LobbyAdapter;

/*
 * This service is for mock and console tests.
 */
@RestController
@RequestMapping("/mock")
public class MockService {
	
	@Autowired
	LobbyAdapter lobbyAdapter;
	
	@Value("${mockService.roomChallenge}")
	boolean enabledMockRoomChallenge;
	
	@RequestMapping(path="/lobby/rooms/{id}", method=RequestMethod.POST)
	public ResponseEntity<ChallengeResponse> challengeRooms(@PathVariable("id") int roomID, @RequestBody String claimToken, @RequestParam(name = "deposit", required = false) Optional<Long> deposit) {
		if(!enabledMockRoomChallenge) {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		try {
			if (roomID <= 0) {
				throw new ValidationException(1, "Invalid RoomID");
			}
			if (claimToken == null || claimToken.length() == 0) {
				throw new ValidationException(2, "Invalid claimToken");
			}
			ChallengeResponse out = lobbyAdapter.challengeRoomByID(1, roomID, claimToken, deposit);
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
