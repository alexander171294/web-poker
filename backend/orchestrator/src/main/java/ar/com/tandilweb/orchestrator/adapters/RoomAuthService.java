package ar.com.tandilweb.orchestrator.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.tandilweb.orchestrator.persistence.repository.RoomsRepository;

@Service
public class RoomAuthService {
	
	@Autowired
	private RoomsRepository roomsRepository;
	
	

}
