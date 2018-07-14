package com.tandilserver.poker_lobby.rest;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tandilserver.poker_lobby.dataBase.domain.Users;
import com.tandilserver.poker_lobby.dataBase.repository.UsersRepository;
import com.tandilserver.poker_lobby.rest.dto.LoginOut;
import com.tandilserver.poker_lobby.rest.dto.RegistroIn;
import com.tandilserver.poker_lobby.rest.dto.Signup;
import com.tandilserver.poker_lobby.services.dto.out.StatusCodes;
import com.tandilserver.poker_lobby.services.utils.ExceptionResponse;

@RestController
@RequestMapping("/userRest")
public class UserRest {

	@Autowired
	UsersRepository usersRepository;
	
	@RequestMapping(path="/helloWorld", method=RequestMethod.GET)
	public String tablatest() {
		return "Hola Mundo";
	}
	
	// TODO: change return type to ResponseEntity<Type>
	@RequestMapping(path="/registro", method=RequestMethod.POST)
	public Signup registrarme(@RequestBody RegistroIn registroData) {
		Signup out = new Signup();
		try {
			if(usersRepository.findByEmail(registroData.email) != null) throw new ExceptionResponse(StatusCodes.ERR, "Email is used");
			if(usersRepository.findByNick(registroData.nick) != null) throw new ExceptionResponse(StatusCodes.ERR, "Nick is used");
			if(registroData.nick.length() < 3) throw new ExceptionResponse(StatusCodes.ERR, "NICK < 3");
			if(registroData.nick.length() > 15) throw new ExceptionResponse(StatusCodes.ERR, "NICK > 15");
			Users user = new Users();
			user.setEmail(registroData.email);
			user.setFecha_registro(new Date());
			user.setFichas(5000L); // fichas iniciales
			user.setNick(registroData.nick);
			// TODO: the password must be encrypted in any hash. 
			user.setPassword(registroData.password);
			user.setUltima_actividad(new Date());
			user.setHashSignature(UUID.randomUUID().toString());
			Users userOut = usersRepository.save(user);
			out.hashSignature = userOut.getHashSignature();
			out.id_usuario = userOut.getId_usuario();
		} catch(ExceptionResponse e) {
			out.statusCode = e.getCode();
			out.message = e.getMessage();
		}
		return out;
	}
	
	// TODO: change return type to ResponseEntity<Type>
	@RequestMapping(path="/login", method=RequestMethod.GET) 
	public LoginOut login(@RequestParam(value="email") String email, @RequestParam(value="password") String password) {
		LoginOut out = new LoginOut();
		Users user = usersRepository.findByEmail(email);
		// TODO: the password must be encrypted in any hash.
		if(user == null || !user.getPassword().equals(password)) {
			out.statusCode = StatusCodes.ERR;
			out.message = "Email or password invalid";
		} else {
			out.id = user.getId_usuario();
			user.setUltima_actividad(new Date());
			usersRepository.save(user);
		}
		return out;
	}
	
}
