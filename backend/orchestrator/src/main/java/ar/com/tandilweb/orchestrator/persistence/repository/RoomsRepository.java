package ar.com.tandilweb.orchestrator.persistence.repository;

import org.springframework.stereotype.Repository;

import ar.com.tandilweb.orchestrator.persistence.BaseRepository;
import ar.com.tandilweb.orchestrator.persistence.domain.Rooms;

@Repository
public class RoomsRepository extends BaseRepository<Rooms, Long> {

	@Override
	public Rooms create(Rooms record) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Rooms record) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Rooms findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
