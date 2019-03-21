package ar.com.tandilweb.ApiServer.rests;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class Login {

	@RequestMapping(path="/signup", method=RequestMethod.GET)
	public String signup() {
		return "";
	}
	
	@RequestMapping(path="/login", method=RequestMethod.POST)
	public String login() {
		return "";
	}
	
}
