package ar.com.tandilweb.ApiServer.rests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ar.com.tandilweb.ApiServer.dataTypesObjects.friends.FriendList;
import ar.com.tandilweb.ApiServer.dataTypesObjects.friends.FriendRequests;
import ar.com.tandilweb.ApiServer.dataTypesObjects.generic.GeneralResponse;
import ar.com.tandilweb.ApiServer.dataTypesObjects.generic.ValidationException;
import ar.com.tandilweb.ApiServer.transport.FriendsAdapter;

@RestController
@RequestMapping("/friends")
public class Friends {
	
	@Autowired
	FriendsAdapter friendsAdapter;
	
	@RequestMapping(path="", method=RequestMethod.GET)
	public ResponseEntity<FriendList> getFriends() {
		FriendList out = new FriendList();
		try {
			out.friends = friendsAdapter.getFriends(0); // from ME
			out.operationSuccess = true;
			return new ResponseEntity<FriendList>(out, HttpStatus.OK);
		} catch (ValidationException e) {
			out.operationSuccess = false;
			out.errorDescription = e.getMessage();
			out.errorCode = e.getIdECode();
			return new ResponseEntity<FriendList>(out, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			out.operationSuccess = false;
			out.errorDescription = e.getMessage();
			out.errorCode = -1;
			return new ResponseEntity<FriendList>(out, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(path="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<GeneralResponse> deleteFriends(@PathVariable("id") int friendID) {
		try {
			if(friendID <= 0) {
				throw new ValidationException(1, "Invalid friend id");
			}
			friendsAdapter.deleteFriend(0, friendID);
			GeneralResponse out = new GeneralResponse();
			out.operationSuccess = true;
			return new ResponseEntity<GeneralResponse>(out, HttpStatus.BAD_REQUEST);
		} catch (ValidationException e) {
			GeneralResponse out = new GeneralResponse();
			out.operationSuccess = false;
			out.errorDescription = e.getMessage();
			out.errorCode = e.getIdECode();
			return new ResponseEntity<GeneralResponse>(out, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			GeneralResponse out = new GeneralResponse();
			out.operationSuccess = false;
			out.errorDescription = e.getMessage();
			out.errorCode = -1;
			return new ResponseEntity<GeneralResponse>(out, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(path="/requests", method=RequestMethod.GET)
	public ResponseEntity<FriendRequests> getFriendsRequest() {
		FriendRequests out = new FriendRequests();
		try {
			out.requests = friendsAdapter.getFriendRequests(0); // from ME
			return new ResponseEntity<FriendRequests>(out, HttpStatus.OK);
		} catch (ValidationException e) {
			out.operationSuccess = false;
			out.errorDescription = e.getMessage();
			out.errorCode = e.getIdECode();
			return new ResponseEntity<FriendRequests>(out, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			out.operationSuccess = false;
			out.errorDescription = e.getMessage();
			out.errorCode = -1;
			return new ResponseEntity<FriendRequests>(out, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(path="/requests/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<GeneralResponse> rejectFriendsRequest(@PathVariable("id") int requestID) {
		try {
			if(requestID <= 0) {
				throw new ValidationException(1, "Invalid request id");
			}
			friendsAdapter.deleteRequest(0, requestID);
			GeneralResponse out = new GeneralResponse();
			out.operationSuccess = true;
			return new ResponseEntity<GeneralResponse>(out, HttpStatus.BAD_REQUEST);
		} catch (ValidationException e) {
			GeneralResponse out = new GeneralResponse();
			out.operationSuccess = false;
			out.errorDescription = e.getMessage();
			out.errorCode = e.getIdECode();
			return new ResponseEntity<GeneralResponse>(out, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			GeneralResponse out = new GeneralResponse();
			out.operationSuccess = false;
			out.errorDescription = e.getMessage();
			out.errorCode = -1;
			return new ResponseEntity<GeneralResponse>(out, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(path="/requests/{id}", method=RequestMethod.PUT)
	public ResponseEntity<GeneralResponse> acceptFriendsRequest(@PathVariable("id") int requestID) {
		try {
			if(requestID <= 0) {
				throw new ValidationException(1, "Invalid request id");
			}
			friendsAdapter.acceptRequest(0, requestID);
			GeneralResponse out = new GeneralResponse();
			out.operationSuccess = true;
			return new ResponseEntity<GeneralResponse>(out, HttpStatus.BAD_REQUEST);
		} catch (ValidationException e) {
			GeneralResponse out = new GeneralResponse();
			out.operationSuccess = false;
			out.errorDescription = e.getMessage();
			out.errorCode = e.getIdECode();
			return new ResponseEntity<GeneralResponse>(out, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			GeneralResponse out = new GeneralResponse();
			out.operationSuccess = false;
			out.errorDescription = e.getMessage();
			out.errorCode = -1;
			return new ResponseEntity<GeneralResponse>(out, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(path="/requests/{id}", method=RequestMethod.POST)
	public ResponseEntity<GeneralResponse> sendFriendsRequest(@PathVariable("id") int userID) {
		try {
			if(userID <= 0) {
				throw new ValidationException(1, "Invalid user id");
			}
			friendsAdapter.sendRequest(0, userID);
			GeneralResponse out = new GeneralResponse();
			out.operationSuccess = true;
			return new ResponseEntity<GeneralResponse>(out, HttpStatus.BAD_REQUEST);
		} catch (ValidationException e) {
			GeneralResponse out = new GeneralResponse();
			out.operationSuccess = false;
			out.errorDescription = e.getMessage();
			out.errorCode = e.getIdECode();
			return new ResponseEntity<GeneralResponse>(out, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			GeneralResponse out = new GeneralResponse();
			out.operationSuccess = false;
			out.errorDescription = e.getMessage();
			out.errorCode = -1;
			return new ResponseEntity<GeneralResponse>(out, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
