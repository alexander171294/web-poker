package com.tandilserver.poker_lobby.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tandilserver.poker_lobby.dataBase.customTypes.ServerTypes;
import com.tandilserver.poker_lobby.dataBase.domain.Rooms;
import com.tandilserver.poker_lobby.dataBase.repository.RoomsRepository;

@RestController
@RequestMapping("/roomsRest")
public class RoomsRest {
	
	@Autowired
	private RoomsRepository roomsRepo;

	@RequestMapping(path="/sitngo", method=RequestMethod.POST)
	public List<Rooms> getRoomsOfSitNGo() {
		return roomsRepo.getRoomsOfType(ServerTypes.SIT_N_GO);
	}
	
	@RequestMapping(path="/tournament", method=RequestMethod.POST)
	public List<Rooms> getRoomsOfTournament() {
		return roomsRepo.getRoomsOfType(ServerTypes.TOURNAMENT);
	}
	
	@RequestMapping(path="/ranked", method=RequestMethod.POST)
	public List<Rooms> getRoomsOfRanked() {
		return roomsRepo.getRoomsOfType(ServerTypes.RANKED);
	}
	
	@RequestMapping(path="/friends", method=RequestMethod.POST)
	public List<Rooms> getRoomsOfFriends() {
		
		return null;
	}
}
