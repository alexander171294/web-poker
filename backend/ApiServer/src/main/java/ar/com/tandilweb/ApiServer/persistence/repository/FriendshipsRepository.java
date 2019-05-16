package ar.com.tandilweb.ApiServer.persistence.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import ar.com.tandilweb.ApiServer.persistence.BaseRepository;
import ar.com.tandilweb.ApiServer.persistence.domain.Friendships;

@Repository
public class FriendshipsRepository extends BaseRepository<Friendships, Long> {

	@Override
	public Friendships create(Friendships record) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Friendships record) {
		// TODO Auto-generated method stub
		
	}
	
	public List<Friendships> getAllFor(long target_origin) {
		return null;
	}

	@Override
	public Friendships findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
