package ar.com.tandilweb.orchestrator.persistence.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import ar.com.tandilweb.orchestrator.persistence.BaseRepository;
import ar.com.tandilweb.orchestrator.persistence.domain.Rooms;

@Repository
public class RoomsRepository extends BaseRepository<Rooms, Long> {
	
	public static Logger logger = LoggerFactory.getLogger(RoomsRepository.class);

	@Override
	public Rooms create(Rooms record) {
		try {
			final String sql = "INSERT INTO rooms "
					+ "(name, accessPassword, securityToken, gproto, max_players, description, minCoinForAccess, recoveryEmail, badLogins, now_connected, isOfficial, server_ip) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
			KeyHolder holder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, record.getName());
					ps.setString(2, record.getAccessPassword());
					ps.setString(3, record.getSecurityToken());
					ps.setString(4, record.getGproto());
					ps.setInt(5, record.getMax_players());
					//ps.setDate(5, new java.sql.Date(record.getLast_login().getTime()));
					ps.setString(6, record.getDescription());
					ps.setInt(7, record.getMinCoinForAccess());
					ps.setString(8, record.getRecoveryEmail());
					ps.setInt(9, record.getBadLogins());
					ps.setBoolean(10, record.isNowConnected());
					ps.setBoolean(11, record.isOfficial());
					ps.setString(12, record.getServer_ip());
					return ps;
				}
			}, holder);
			record.setId_room(holder.getKey().longValue());
			return record;
		} catch(DataAccessException e) {
			logger.error("RoomsRepository::create", e);
			return null;
		}
	}

	@Override
	public void update(Rooms record) {
		try {
			final String sql = "UPDATE rooms SET name = ?, securityToken = ?, server_ip = ? WHERE id_room = ?";
			jdbcTemplate.update(sql, new Object[] {
					record.getName(),
					record.getSecurityToken(),
					record.getServer_ip(),
					record.getId_room()
			});
		} catch(DataAccessException e) {
			logger.error("RoomsRepository::update", e);
		}
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
	        		rs.getBoolean("nowConnected"),
	        		rs.getBoolean("isOfficial")
	        		);
	    }
	}
	
}
