package ar.com.tandilweb.ApiServer.persistence.repository;

import org.springframework.stereotype.Repository;

import ar.com.tandilweb.ApiServer.persistence.BaseRepository;
import ar.com.tandilweb.ApiServer.persistence.domain.Users;

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
	
	public boolean checkEmailUser(String email, String nick_name) {
		return false;
	}
}
