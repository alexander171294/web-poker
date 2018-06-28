package com.tandilserver.poker_lobby.rest;

import java.util.Date;

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
import com.tandilserver.poker_lobby.services.dto.out.Response;
import com.tandilserver.poker_lobby.services.dto.out.StatusCodes;

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
	public Response registrarme(@RequestBody RegistroIn registroData) {
		Response out = new Response();
		
		// TODO: alexander.eberle - Check if this nick/email is already registered.
		Users user = new Users();
		user.setEmail(registroData.email);
		user.setFecha_registro(new Date());
		user.setFichas(5000L); // fichas iniciales
		user.setNick(registroData.nick);
		user.setPassword(registroData.password);
		user.setUltima_actividad(new Date());
		Users userOut = usersRepository.save(user);
		
		return out;
	}
	
	@RequestMapping(path="/login", method=RequestMethod.GET) 
	public LoginOut login(@RequestParam(value="email") String email, @RequestParam(value="password") String password) {
		LoginOut out = new LoginOut();
		
		Users user = usersRepository.findByEmail(email);
		if(user == null || !user.getPassword().equals(password)) {
			out.statusCode = StatusCodes.ERR;
			out.message = "Email o clave incorrectos";
		} else {
			out.id = user.getId_usuario();
			user.setUltima_actividad(new Date());
			usersRepository.save(user);
		}
		
		return out;
	}
	
}
