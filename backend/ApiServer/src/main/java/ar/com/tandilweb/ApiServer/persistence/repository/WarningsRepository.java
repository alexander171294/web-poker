package ar.com.tandilweb.ApiServer.persistence.repository;

import org.springframework.stereotype.Repository;

import ar.com.tandilweb.ApiServer.persistence.BaseRepository;
import ar.com.tandilweb.ApiServer.persistence.domain.Warnings;

@Repository
public class WarningsRepository extends BaseRepository<Warnings, Long> {

	@Override
	public Warnings create(Warnings record) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Warnings record) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Warnings findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
