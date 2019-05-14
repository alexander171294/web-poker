package ar.com.tandilweb.ApiServer.transport;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ar.com.tandilweb.ApiServer.dataTypesObjects.generic.ValidationException;
import ar.com.tandilweb.ApiServer.dataTypesObjects.login.SessionInformation;
import ar.com.tandilweb.ApiServer.dataTypesObjects.login.SignupRequest;
import ar.com.tandilweb.ApiServer.persistence.domain.Sessions;
import ar.com.tandilweb.ApiServer.persistence.domain.Users;
import ar.com.tandilweb.ApiServer.persistence.repository.SessionsRepository;
import ar.com.tandilweb.ApiServer.persistence.repository.UsersRepository;

@Service
public class LoginAdapter {
	
	@Autowired 
	private UsersRepository userRepository;
	
	@Autowired
	private SessionsRepository sessionsRepository;
	
	@Value("${ar.com.tandilweb.ApiServer.users.initialChips}")
	private long initialChips;
	
	public SessionInformation signup(SignupRequest signupData) throws ValidationException {
		if(userRepository.checkEmailUser(signupData.email, signupData.nick_name)) {
			throw new ValidationException(4, "Email o nick are in use");
		}
		Users user = new Users();
		user.setBadLogins((short)0);
		user.setChips(initialChips);
		user.setEmail(signupData.email);
		user.setNick_name(signupData.nick_name);
		user.setPassword(signupData.password);
		user.setPhoto(signupData.photo != null ? signupData.photo : "#");
		user = userRepository.create(user);
		// create session for new user:
		Sessions session = new Sessions();
		//session.setExpiration(expiration); // dentro de 10 min
		session.setId_user(user.getId_user());
		String sessionPassphrase = UUID.randomUUID().toString();
		session.setJwt_passphrase(sessionPassphrase);
		session = sessionsRepository.create(session);
		SessionInformation out = new SessionInformation();
		out.operationSuccess = true;
		out.jwtPasspharse = sessionPassphrase;
		out.sessionID = session.getId_session();
		out.userID = session.getId_user();
		return out;
	}

}
