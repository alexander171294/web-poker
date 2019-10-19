package ar.com.tandilweb.persistence.repository;

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

import ar.com.tandilweb.persistence.BaseRepository;
import ar.com.tandilweb.persistence.domain.Challenges;

public class ChallengesRepository extends BaseRepository<Challenges, Long> {
	
	public static Logger logger = LoggerFactory.getLogger(ChallengesRepository.class);

	@Override
	public Challenges create(final Challenges record) {
		try {
			final String sql = "INSERT INTO challenges "
					+ "(id_user, id_room, challenge) "
					+ "VALUES(?,?,?)";
			KeyHolder holder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setLong(1, record.getId_user());
					ps.setLong(2, record.getId_room());
					ps.setString(3, record.getChallenge());
					return ps;
				}
			}, holder);
			record.setChallengeID(holder.getKey().longValue());
			return record;
		} catch(DataAccessException e) {
			logger.error("ChallengesRepository::create", e);
			return null;
		}
	}

	@Override
	@Deprecated
	public void update(Challenges record) {
		// you can't update challenges.
		logger.warn("Deprecated update function called. (Code Ignored)");
	}

	@Override
	public Challenges findById(Long id) {
		try {
			return jdbcTemplate.queryForObject(
                "SELECT * FROM challenges WHERE challengeID = ?",
                new Object[]{id}, new ChallengesRowMapper());
		} catch(DataAccessException e) {
			return null;
		}
	}
	
	class ChallengesRowMapper implements RowMapper<Challenges> {
		public Challenges mapRow(ResultSet rs, int rowNum) throws SQLException {
	        return new Challenges(
	        		rs.getLong("challengeID"),
	        		rs.getLong("id_user"),
	        		rs.getLong("id_room"),
	        		rs.getString("challenge") // claimToken
	        		);
	    }
	}
	
}
