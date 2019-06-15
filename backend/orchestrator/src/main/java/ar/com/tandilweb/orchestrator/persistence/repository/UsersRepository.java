package ar.com.tandilweb.orchestrator.persistence.repository;

import org.springframework.stereotype.Repository;

import ar.com.tandilweb.orchestrator.persistence.BaseRepository;
import ar.com.tandilweb.orchestrator.persistence.domain.Users;

@Repository
public class UsersRepository extends BaseRepository<Users, Long> {

	@Override
	public Users create(Users record) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Users record) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Users findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
