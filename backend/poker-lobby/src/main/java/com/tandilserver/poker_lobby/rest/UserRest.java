package com.tandilserver.poker_lobby.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userRest")
public class UserRest {

	@RequestMapping(path="/helloWorld", method=RequestMethod.GET)
	public String tablatest() {
		return "Hola Mundo";
	}
	
}
