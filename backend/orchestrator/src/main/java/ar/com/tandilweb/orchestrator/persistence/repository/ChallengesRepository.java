package ar.com.tandilweb.orchestrator.persistence.repository;

import org.springframework.stereotype.Repository;

import ar.com.tandilweb.orchestrator.persistence.BaseRepository;
import ar.com.tandilweb.orchestrator.persistence.domain.Challenges;

@Repository
public class ChallengesRepository extends BaseRepository<Challenges, Long> {

	@Override
	public Challenges create(Challenges record) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Challenges record) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Challenges findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
