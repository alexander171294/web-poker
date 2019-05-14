package ar.com.tandilweb.ApiServer.rests;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class Users {
	
	@RequestMapping(path="/users/profile/{id}", method=RequestMethod.GET)
	public String getProfile(@PathVariable("id") int userID) {
		return "";
	}
	
	@RequestMapping(path="/users/profile/{id}", method=RequestMethod.PUT)
	public String updateProfile(@PathVariable("id") int userID) {
		return "";
	}

}
