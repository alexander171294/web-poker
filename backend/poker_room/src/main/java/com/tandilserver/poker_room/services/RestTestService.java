package com.tandilserver.poker_room.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tandilserver.poker_room.registerService.ServerDataBlock;

@RestController
@RequestMapping("/demoRest")
public class RestTestService {
	
	@Autowired
	ServerDataBlock srvDataBlock;
	
	@RequestMapping(path="/updateSrvData", method=RequestMethod.POST)
	public void updateSrvData() {
		this.srvDataBlock.getSrvInfo().players++;
	}

}
