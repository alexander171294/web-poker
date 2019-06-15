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
import ar.com.tandilweb.ApiServer.persistence.domain.Friendships;

@Repository
public class FriendshipsRepository extends BaseRepository<Friendships, Long> {

	public static Logger logger = LoggerFactory.getLogger(FriendshipsRepository.class);
	
	@Override
	public Friendships create(Friendships record) {
		try {
			final String sql = "INSERT INTO friendships "
					+ "(id_user_origin, id_user_target, requested, accepted) "
					+ "VALUES(?,?,?,?)";
			jdbcTemplate.update(sql, new Object[] {
					record.getId_user_origin(),
					record.getId_user_target(),
					record.getRequested(),
					record.isAccepted()
			});
			return record;
		} catch(DataAccessException e) {
			logger.error("FriendshipsRepository::create", e);
			return null;
		}
	}

	@Override
	public void update(Friendships record) {
		try {
			final String sql = "UPDATE friendships SET accepted = ? WHERE id_user_origin = ? AND id_user_target = ?";
			jdbcTemplate.update(sql, new Object[] {
					record.isAccepted(),
					record.getId_user_origin(),
					record.getId_user_target()
			});
		} catch(DataAccessException e) {
			logger.error("FriendshipsRepository::update", e);
		}
	}
	
	public List<Friendships> getAllFor(long target_origin) {
		try {
			return jdbcTemplate.query(
                "SELECT * FROM friendships WHERE id_user_origin = ? OR id_user_target = ?",
                new Object[]{target_origin, target_origin}, new FriendshipsRowMapper());
		} catch(DataAccessException e) {
			return null;
		}
	}
	
	public Friendships findFor(long me, long target_origin) {
		try {
			return jdbcTemplate.queryForObject(
                "SELECT * FROM friendships WHERE (id_user_origin = ? AND id_user_target = ?) OR (id_user_origin = ? AND id_user_target = ?)",
                new Object[]{me, target_origin, target_origin, me}, new FriendshipsRowMapper());
		} catch(DataAccessException e) {
			return null;
		}
	}
	
	public Friendships findByOriginTarget(long origin, long target) {
		try {
			return jdbcTemplate.queryForObject(
                "SELECT * FROM friendships WHERE id_user_origin = ? AND id_user_target = ?",
                new Object[]{origin, target}, new FriendshipsRowMapper());
		} catch(DataAccessException e) {
			return null;
		}
	}

	@Override
	@Deprecated
	public Friendships findById(Long id) {
		return null;
	}
	
	public void delete(Friendships record) {
		try {
			final String sql = "DELETE FROM friendships WHERE id_user_origin = ? AND id_user_target = ?";
			jdbcTemplate.update(sql, new Object[] {
					record.getId_user_origin(),
					record.getId_user_target()
			});
		} catch(DataAccessException e) {
			logger.error("FriendshipsRepository::delete", e);
		}
	}
	
	class FriendshipsRowMapper implements RowMapper<Friendships> {
		public Friendships mapRow(ResultSet rs, int rowNum) throws SQLException {
	        return new Friendships(
	        		rs.getLong("id_user_origin"),
	        		rs.getLong("id_user_target"),
	        		rs.getDate("requested"),
	        		rs.getBoolean("accepted")
	        		);
	    }
	}
	
}
