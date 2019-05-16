package ar.com.tandilweb.ApiServer.persistence.repository;

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

import ar.com.tandilweb.ApiServer.persistence.BaseRepository;
import ar.com.tandilweb.ApiServer.persistence.domain.Sessions;
import ar.com.tandilweb.ApiServer.persistence.domain.Users;
import ar.com.tandilweb.ApiServer.persistence.repository.UsersRepository.UsersRowMapper;

@Repository
public class SessionsRepository extends BaseRepository<Sessions, Long> {
	
	public static Logger logger = LoggerFactory.getLogger(SessionsRepository.class);

	@Override
	public Sessions create(Sessions record) {
		try {
			final String sql = "INSERT INTO sessions "
					+ "(id_user, jwt_passphrase, expiration) "
					+ "VALUES(?,?,?)";
			KeyHolder holder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setLong(1, record.getId_user());
					ps.setString(2, record.getJwt_passphrase());
					if(record.getExpiration() != null) { // Optional
						ps.setDate(3, new java.sql.Date(record.getExpiration().getTime()));
					} else {
						ps.setDate(3, null);
					}
					return ps;
				}
			}, holder);
			record.setId_session(holder.getKey().longValue());
			return record;
		} catch(DataAccessException e) {
			logger.error("SessionsRepository::create", e);
			return null;
		}
	}

	@Override
	public void update(Sessions record) {
		try {
			final String sql = "UPDATE sessions SET expiration = ?, jwt_passphrase = ? WHERE id_session = ?";
			jdbcTemplate.update(sql, new Object[] {
					record.getExpiration(),
					record.getJwt_passphrase()
			});
		} catch(DataAccessException e) {
			logger.error("SessionsRepository::update", e);
		}
	}

	@Override
	public Sessions findById(Long id) {
		try {
			return jdbcTemplate.queryForObject(
                "SELECT * FROM sessions WHERE id_session = ?",
                new Object[]{id}, new SessionsRowMapper());
		} catch(DataAccessException e) {
			return null;
		}
	}
	
	class SessionsRowMapper implements RowMapper<Sessions> {
		public Sessions mapRow(ResultSet rs, int rowNum) throws SQLException {
	        return new Sessions(
	        		rs.getLong("id_session"),
	        		rs.getLong("id_user"),
	        		rs.getString("jwt_passphrase"),
	        		rs.getDate("expiration")
	        		);
	    }
	}
}
