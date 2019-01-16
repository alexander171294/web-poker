package com.tandilserver.poker_lobby.dataBase.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tandilserver.poker_lobby.dataBase.BaseRepository;
import com.tandilserver.poker_lobby.dataBase.domain.Friendships;
import com.tandilserver.poker_lobby.dataBase.domain.StatusFriendships;
import com.tandilserver.poker_lobby.dataBase.domain.Users;
import com.tandilserver.poker_lobby.dataBase.repository.UsersRepository.UsersRowMapper;

@Repository
public class FriendshipsRepository extends BaseRepository<Friendships, Long> {
	
	public static Logger logger = LoggerFactory.getLogger(FriendshipsRepository.class);

	@Override
	public Friendships create(Friendships record) {
		try {
			final String sql = "INSERT INTO friendships"
					+ "(id_requester, id_target, status, request_date)"
					+ " VALUES(?,?,?,?)";
			jdbcTemplate.update(sql, new Object[] {
					record.getId_requester(),
					record.getId_target(),
					record.getStatus().ordinal(),
					record.getRequest_date()
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
			final String sql = "UPDATE friendships SET "
					+ "status = ? WHERE id_requester = ? AND id_target";
			jdbcTemplate.update(sql, new Object[] {
					record.getStatus().ordinal(),
					record.getId_requester(),
					record.getId_target()
			});
		} catch(DataAccessException e) {
			logger.error("FriendshipsRepository::update", e);
		}
	}

	@Override
	public Friendships findById(Long id) {
		return null;
	}
	
	public List<Friendships> findByRequester(Long id) {
		try {
			return jdbcTemplate.query(
                "SELECT * FROM friendships WHERE id_requester = ?",
                new Object[]{ id }, new FriendshipsRowMapper());
		} catch(DataAccessException e) {
			return null;
		}
	}
	
	public List<Friendships> findBySender(Long id) {
		try {
			return jdbcTemplate.query(
                "SELECT * FROM friendships WHERE id_sender = ?",
                new Object[]{ id }, new FriendshipsRowMapper());
		} catch(DataAccessException e) {
			return null;
		}
	}
	
	public List<Friendships> findByBoth(Long id_requester, Long id_sender) {
		try {
			return jdbcTemplate.query(
                "SELECT * FROM friendships WHERE id_sender = ? AND id_requester = ?",
                new Object[]{ id_sender, id_requester }, new FriendshipsRowMapper());
		} catch(DataAccessException e) {
			return null;
		}
	}
	
	class FriendshipsRowMapper implements RowMapper<Friendships>
	{
	    public Friendships mapRow(ResultSet rs, int rowNum) throws SQLException {
	        return new Friendships(
	        		rs.getLong("id_requester"),
	        		rs.getLong("id_target"),
	        		StatusFriendships.values()[rs.getInt("status")],
	        		rs.getDate("request_date")
	        		);
	    }
	}

}


