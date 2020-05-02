package ar.com.tandilweb.ApiServer.rests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ar.com.tandilweb.ApiServer.dataTypesObjects.generic.ValidationException;
import ar.com.tandilweb.ApiServer.dataTypesObjects.users.UserProfile;
import ar.com.tandilweb.ApiServer.dataTypesObjects.users.UserProfileEdited;
import ar.com.tandilweb.ApiServer.transport.UsersAdapter;
import ar.com.tandilweb.persistence.domain.Sessions;

@RestController
@RequestMapping("/users")
public class Users {
	
	@Autowired
	UsersAdapter usersAdapter;
	
	@RequestMapping(path="/profile/{id}", method=RequestMethod.GET)
	public ResponseEntity<UserProfile> getProfile(@PathVariable("id") long userID, @RequestAttribute("jwtSessionOrigin") Sessions session) {
		try {
			if(userID <= 0) {
				throw new ValidationException(1, "Invalid user id");
			}
			UserProfile up = usersAdapter.getUserByID(userID);
			return new ResponseEntity<UserProfile>(up, HttpStatus.OK);
		} catch (ValidationException e) {
			UserProfile out = new UserProfile();
			out.operationSuccess = false;
			out.errorDescription = e.getMessage();
			out.errorCode = e.getIdECode();
			return new ResponseEntity<UserProfile>(out, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			UserProfile out = new UserProfile();
			out.operationSuccess = false;
			out.errorDescription = e.getMessage();
			out.errorCode = -1;
			return new ResponseEntity<UserProfile>(out, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(path="/profile/{id}", method=RequestMethod.PUT)
	public ResponseEntity<UserProfile> updateProfile(@PathVariable("id") long userID, @RequestBody UserProfileEdited userProfile) {
		try {
			if(userID <= 0) {
				throw new ValidationException(1, "Invalid user id");
			}
			// FIXME: validate email
			if(userProfile.email != null && userProfile.email.length() > 0 && userProfile.email.length() < 5) {
				throw new ValidationException(2, "Invalid email");
			}
			if(userProfile.password != null && userProfile.password.length() > 0 && userProfile.password.length() < 8) {
				throw new ValidationException(3, "Password must be more than 8 characters.");
			}
			UserProfile up = usersAdapter.updateProfileByID(userID, userProfile);
			return new ResponseEntity<UserProfile>(up, HttpStatus.BAD_REQUEST);
		} catch (ValidationException e) {
			UserProfile out = new UserProfile();
			out.operationSuccess = false;
			out.errorDescription = e.getMessage();
			out.errorCode = e.getIdECode();
			return new ResponseEntity<UserProfile>(out, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			UserProfile out = new UserProfile();
			out.operationSuccess = false;
			out.errorDescription = e.getMessage();
			out.errorCode = -1;
			return new ResponseEntity<UserProfile>(out, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(path="/profile/getChips/{id}", method=RequestMethod.GET)
	public ResponseEntity<Long> getChips(@PathVariable("id") long userID, @RequestAttribute("jwtSessionOrigin") Sessions session) {
		try {
			if(userID <= 0) {
				throw new ValidationException(1, "Invalid user id");
			}
			UserProfile up = usersAdapter.getUserByID(userID);
			return new ResponseEntity<Long>(up.chips, HttpStatus.OK);
		} catch (ValidationException e) {
			return new ResponseEntity<Long>((long) 0, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<Long>((long) 0, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
