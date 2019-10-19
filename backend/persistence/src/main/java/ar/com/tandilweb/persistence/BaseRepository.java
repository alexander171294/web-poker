package ar.com.tandilweb.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class BaseRepository<t, pk> {

	@Autowired
	protected JdbcTemplate jdbcTemplate;
	
	public abstract t create(t record);
	public abstract void update(t record);
	public abstract t findById(pk id);

}
