package ar.com.tandilweb.orchestrator.adapters;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.tandilweb.orchestrator.persistence.repository.UsersInRoomsRepository;
import ar.com.tandilweb.persistence.domain.UsersInRooms;

@Service
public class UserControlService {
	
	protected static Logger logger = LoggerFactory.getLogger(UserControlService.class);
	
	@Autowired
	private UsersInRoomsRepository uirRepo;
	
	public void addUserInRoom(Long userID, Long roomID) {
		UsersInRooms record = new UsersInRooms();
		record.setId_room(roomID);
		record.setId_user(userID);
		record.setPosition(-1);
		record.setRegistered(new Date());
		uirRepo.create(record);
	}
	
	public void removeUsersInRoom(Long roomID) {
		uirRepo.removeAll(roomID);
	}

}
