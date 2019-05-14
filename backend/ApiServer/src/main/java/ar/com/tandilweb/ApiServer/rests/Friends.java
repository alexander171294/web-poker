package ar.com.tandilweb.ApiServer.rests;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/friends")
public class Friends {
	
	@RequestMapping(path="", method=RequestMethod.GET)
	public String getFriends() {
		return "";
	}
	
	@RequestMapping(path="/{id}", method=RequestMethod.DELETE)
	public String deleteFriends(@PathVariable("id") int friendID) {
		return "";
	}
	
	@RequestMapping(path="/requests", method=RequestMethod.DELETE)
	public String getFriendsRequest() {
		return "";
	}
	
	@RequestMapping(path="/requests/{id}", method=RequestMethod.PUT)
	public String responseFriendsRequest(@PathVariable("id") int requestID) {
		return "";
	}
	
	@RequestMapping(path="/requests/{id}", method=RequestMethod.POST)
	public String sendFriendsRequest(@PathVariable("id") int requestID) {
		return "";
	}
	
}
