package ar.com.tandilweb.ApiServer.rests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ar.com.tandilweb.ApiServer.dataTypesObjects.generic.ValidationException;
import ar.com.tandilweb.ApiServer.dataTypesObjects.login.LoginRequest;
import ar.com.tandilweb.ApiServer.dataTypesObjects.login.SessionInformation;
import ar.com.tandilweb.ApiServer.dataTypesObjects.login.SignupRequest;
import ar.com.tandilweb.ApiServer.transport.LoginAdapter;

@RestController
@RequestMapping("/public")
public class Login {
	
	@Autowired
	private LoginAdapter loginAdapter;

	@RequestMapping(path="/signup", method=RequestMethod.POST)
	public ResponseEntity<SessionInformation> signup(@RequestBody SignupRequest signupData) {
		SessionInformation out = new SessionInformation();
		try {
			if(signupData.nick_name == null || signupData.nick_name.length() < 3 || signupData.nick_name.length() > 12) {
				throw new ValidationException(0, "Invalid nickname");
			}
			// FIXME: validate email
			if(signupData.email == null) {
				throw new ValidationException(1, "Invalid email");
			}
			if(signupData.password == null || signupData.password.length() < 8) {
				throw new ValidationException(2, "Password must be more than 8 characters.");
			}
			return new ResponseEntity<SessionInformation>(
				loginAdapter.signup(signupData),
				HttpStatus.OK
			);
		} catch (ValidationException e) {
			out.operationSuccess = false;
			out.errorDescription = e.getMessage();
			out.errorCode = e.getIdECode();
			return new ResponseEntity<SessionInformation>(out, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			out.operationSuccess = false;
			out.errorDescription = e.getMessage();
			out.errorCode = -1;
			return new ResponseEntity<SessionInformation>(out, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(path="/login", method=RequestMethod.POST)
	public ResponseEntity<SessionInformation> login(@RequestBody LoginRequest loginData) {
		return null;
	}
	
}
