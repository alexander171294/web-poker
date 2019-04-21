package ar.com.tandilweb.ApiServer.persistence.repository;

import org.springframework.stereotype.Repository;

import ar.com.tandilweb.ApiServer.persistence.BaseRepository;
import ar.com.tandilweb.ApiServer.persistence.domain.UsersBanned;

@Repository
public class UsersBannedRepository extends BaseRepository<UsersBanned, Long> {

	@Override
	public UsersBanned create(UsersBanned record) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(UsersBanned record) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UsersBanned findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
