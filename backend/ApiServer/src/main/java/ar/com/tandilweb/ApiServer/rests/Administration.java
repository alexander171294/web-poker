package ar.com.tandilweb.ApiServer.rests;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/administration")
public class Administration {
	
	@RequestMapping(path="/banlist", method=RequestMethod.GET)
	public String getBanlist() {
		return "";
	}
	
	@RequestMapping(path="/warnlist", method=RequestMethod.GET)
	public String getWarnlist() {
		return "";
	}
	
	@RequestMapping(path="/rooms-registered", method=RequestMethod.GET)
	public String getRoomsRegistered() {
		return "";
	}
	
	@RequestMapping(path="/banlist/{id}", method=RequestMethod.POST)
	public String addBan() {
		return "";
	}
	
	@RequestMapping(path="/warnlist/{id}", method=RequestMethod.POST)
	public String addWarn() {
		return "";
	}
	
	@RequestMapping(path="/warnlist/{id}", method=RequestMethod.PUT)
	public String updateWarn() {
		return "";
	}
	
	@RequestMapping(path="/banlist/{id}", method=RequestMethod.PUT)
	public String updateBan() {
		return "";
	}
	
	@RequestMapping(path="/banlist/{id}", method=RequestMethod.DELETE)
	public String deleteBan() {
		return "";
	}
	
	@RequestMapping(path="/warnlist/{id}", method=RequestMethod.DELETE)
	public String deleteWarning() {
		return "";
	}
	
	@RequestMapping(path="/rooms-registered/{id}", method=RequestMethod.DELETE)
	public String deleteRoomServer() {
		return "";
	}

}
