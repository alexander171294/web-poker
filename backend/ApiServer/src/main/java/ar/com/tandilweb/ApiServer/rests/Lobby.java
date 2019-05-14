package ar.com.tandilweb.ApiServer.rests;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lobby")
public class Lobby {
	
	@RequestMapping(path="/rooms", method=RequestMethod.GET)
	public String getRooms() {
		return "";
	}
	
	@RequestMapping(path="/rooms/{id}", method=RequestMethod.GET)
	public String getRoomsByID(@PathVariable("id") int roomID) {
		return "";
	}
	
	@RequestMapping(path="/rooms/{id}", method=RequestMethod.POST)
	public String challengeRooms(@PathVariable("id") int roomID) {
		return "";
	}
	
}
