package ar.com.tandilweb.ApiServer.rests;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.com.tandilweb.ApiServer.dataTypesObjects.generic.ValidationException;
import ar.com.tandilweb.ApiServer.dataTypesObjects.login.LoginRequest;
import ar.com.tandilweb.ApiServer.dataTypesObjects.login.SessionInformation;
import ar.com.tandilweb.ApiServer.dataTypesObjects.login.SignupRequest;
import ar.com.tandilweb.ApiServer.dataTypesObjects.login.ValidateRequest;
import ar.com.tandilweb.ApiServer.transport.LoginAdapter;

@RestController
@RequestMapping("/public")
public class Login {
	
	@Autowired
	private LoginAdapter loginAdapter;

	@RequestMapping(path="/signup", method=RequestMethod.POST)
	public ResponseEntity<SessionInformation> signup(@RequestBody SignupRequest signupData) {
		try {
			if(signupData.nick_name == null || signupData.nick_name.length() < 3 || signupData.nick_name.length() > 12) {
				throw new ValidationException(1, "Invalid nickname");
			}
			// FIXME: validate email
			if(signupData.email == null) {
				throw new ValidationException(2, "Invalid email");
			}
			if(signupData.password == null || signupData.password.length() < 8) {
				throw new ValidationException(3, "Password must be more than 8 characters.");
			}
			return new ResponseEntity<SessionInformation>(
				loginAdapter.signup(signupData),
				HttpStatus.OK
			);
		} catch (ValidationException e) {
			SessionInformation out = new SessionInformation();
			out.operationSuccess = false;
			out.errorDescription = e.getMessage();
			out.errorCode = e.getIdECode();
			return new ResponseEntity<SessionInformation>(out, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			SessionInformation out = new SessionInformation();
			out.operationSuccess = false;
			out.errorDescription = e.getMessage();
			out.errorCode = -1;
			return new ResponseEntity<SessionInformation>(out, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(path="/login", method=RequestMethod.POST)
	public ResponseEntity<SessionInformation> login(@Valid @RequestBody LoginRequest loginData) {
		try {
			
			if(loginData.umail == null || loginData.umail.length() < 3) {
				throw new ValidationException(1, "Invalid nickname or email");
			}
			if(loginData.password == null || loginData.password.length() < 8) {
				throw new ValidationException(2, "Invalid password.");
			}
			return new ResponseEntity<SessionInformation>(
				loginAdapter.login(loginData),
				HttpStatus.OK
			);
		} catch (ValidationException e) {
			SessionInformation out = new SessionInformation();
			out.operationSuccess = false;
			out.errorDescription = e.getMessage();
			out.errorCode = e.getIdECode();
			return new ResponseEntity<SessionInformation>(out, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			SessionInformation out = new SessionInformation();
			out.operationSuccess = false;
			out.errorDescription = e.getMessage();
			out.errorCode = -1;
			return new ResponseEntity<SessionInformation>(out, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(path="/validate", method=RequestMethod.POST)
	public ResponseEntity<SessionInformation> validate(@RequestBody ValidateRequest validateData) {
	try {
			if(validateData.umail == null || validateData.umail.length() < 3) {
				throw new ValidationException(1, "Invalid nickname or email");
			}
			if(validateData.password == null || validateData.password.length() < 8) {
				throw new ValidationException(2, "Invalid password.");
			}
			return new ResponseEntity<SessionInformation>(
				loginAdapter.validate(validateData),
				HttpStatus.OK
			);
		} catch (ValidationException e) {
			SessionInformation out = new SessionInformation();
			out.operationSuccess = false;
			out.errorDescription = e.getMessage();
			out.errorCode = e.getIdECode();
			return new ResponseEntity<SessionInformation>(out, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			SessionInformation out = new SessionInformation();
			out.operationSuccess = false;
			out.errorDescription = e.getMessage();
			out.errorCode = -1;
			return new ResponseEntity<SessionInformation>(out, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
//	@RequestMapping(path="/generateValidationCode", method=RequestMethod.GET)
//	public ResponseEntity<String> generateValidationCode(@RequestParam String email){
//		try {			
//			loginAdapter.generateValidationCode(email);
//			return null;
//		} catch (ValidationException e) {
//			SessionInformation out = new SessionInformation();
//			out.operationSuccess = false;
//			out.errorDescription = e.getMessage();
//			out.errorCode = e.getIdECode();
//			return new ResponseEntity<SessionInformation>(out, HttpStatus.BAD_REQUEST);
//		} catch (Exception e) {
//			SessionInformation out = new SessionInformation();
//			out.operationSuccess = false;
//			out.errorDescription = e.getMessage();
//			out.errorCode = -1;
//			return new ResponseEntity<SessionInformation>(out, HttpStatus.INTERNAL_SERVER_ERROR);
//		} 
//	}
	
}
