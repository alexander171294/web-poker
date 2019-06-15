package ar.com.tandilweb.ApiServer.persistence.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ar.com.tandilweb.ApiServer.persistence.BaseRepository;
import ar.com.tandilweb.ApiServer.persistence.domain.Rooms;

@Repository
public class RoomsRepository extends BaseRepository<Rooms, Long> {
	
	public static Logger logger = LoggerFactory.getLogger(RoomsRepository.class);

	@Override
	public Rooms create(Rooms record) {
		return null;
	}

	@Override
	public void update(Rooms record) {
		
	}

	@Override
	public Rooms findById(Long id) {
		try {
			return jdbcTemplate.queryForObject(
                "SELECT * FROM rooms WHERE id_room = ?",
                new Object[]{id}, new RoomsRowMapper());
		} catch(DataAccessException e) {
			return null;
		}
	}
	
	public List<Rooms> getAll() {
		try {
			return jdbcTemplate.query(
                "SELECT * FROM rooms", new RoomsRowMapper());
		} catch(DataAccessException e) {
			return null;
		}
	}
	
	public List<Rooms> getConnecteds() {
		try {
			return jdbcTemplate.query(
                "SELECT * FROM rooms WHERE now_connected = 1", new RoomsRowMapper());
		} catch(DataAccessException e) {
			return null;
		}
	}
	
	public List<Rooms> getByGproto(String gproto) {
		try {
			return jdbcTemplate.query(
                "SELECT * FROM rooms WHERE gproto = ?",
                new Object[]{ gproto }, new RoomsRowMapper());
		} catch(DataAccessException e) {
			return null;
		}
	}
	
	class RoomsRowMapper implements RowMapper<Rooms> {
		public Rooms mapRow(ResultSet rs, int rowNum) throws SQLException {
	        return new Rooms(
	        		rs.getLong("id_room"),
	        		rs.getString("server_ip"),
	        		rs.getString("name"),
	        		rs.getString("accessPassword"),
	        		rs.getString("securityToken"),
	        		rs.getString("gproto"),
	        		rs.getInt("max_players"),
	        		rs.getString("description"),
	        		rs.getInt("minCoinForAccess"),
	        		rs.getString("recoveryEmail"),
	        		rs.getInt("badLogins"),
	        		rs.getBoolean("now_connected"),
	        		rs.getBoolean("isOfficial")
	        		);
	    }
	}
	
}
