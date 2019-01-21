package com.tandilserver.poker_lobby.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tandilserver.poker_lobby.dataBase.customTypes.ServerTypes;
import com.tandilserver.poker_lobby.dataBase.domain.Rooms;
import com.tandilserver.poker_lobby.dataBase.repository.RoomsRepository;
import com.tandilserver.poker_lobby.rest.dto.RoomsOut;

@RestController
@RequestMapping("/roomsRest")
public class RoomsRest {
	
	@Autowired
	private RoomsRepository roomsRepo;

	@RequestMapping(path="/sitngo", method=RequestMethod.GET)
	public List<RoomsOut> getRoomsOfSitNGo() {
		List<Rooms> rooms = roomsRepo.getRoomsOfType(ServerTypes.SIT_N_GO);
		List<RoomsOut> out = new ArrayList<RoomsOut>();
		for(Rooms room: rooms) {
			RoomsOut item = new RoomsOut();
			item.setBlind(room.getBlind());
			item.setId_server(room.getId_server());
			item.setIp(room.getIp());
			item.setLimit_bet(room.getLimit_bet());
			item.setMin_bet(room.getMin_bet());
			item.setOfficialServer(room.isOfficialServer());
			item.setPlayers(room.getPlayers());
			item.setPort(room.getPort());
			item.setServer_name(room.getServer_name());
			item.setServer_type(room.getServer_type());
			out.add(item);
		}
		return out;
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
