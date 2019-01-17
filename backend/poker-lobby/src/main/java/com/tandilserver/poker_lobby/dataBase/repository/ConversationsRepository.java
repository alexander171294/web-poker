package com.tandilserver.poker_lobby.dataBase.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tandilserver.poker_lobby.dataBase.BaseRepository;
import com.tandilserver.poker_lobby.dataBase.domain.Conversations;

@Repository
public class ConversationsRepository extends BaseRepository<Conversations, Long>{
	
	public static Logger logger = LoggerFactory.getLogger(ConversationsRepository.class);
	
	@Autowired
	UsersRepository usersRepository;

	@Override
	public Conversations create(Conversations record) {
		try {
			final String sql = "INSERT INTO conversations"
					+ "(id_conversation, user_origin, user_target, last_message)"
					+ " VALUES(?,?,?,?)";
			jdbcTemplate.update(sql, new Object[] {
					record.getId_conversation(),
					record.getUser_origin().getId_usuario(),
					record.getUser_target().getId_usuario(),
					record.getLast_message()
					});
			return record;
		} catch(DataAccessException e) {
			logger.error("ConversationsRepository::create", e);
			return null;
		}
	}

	@Override
	public void update(Conversations record) {
		try {
			final String sql = "UPDATE conversations SET "
					+ "last_message = ? WHERE id_conversation = ?";
			jdbcTemplate.update(sql, new Object[] {
					record.getLast_message(),
					record.getId_conversation()
			});
		} catch(DataAccessException e) {
			logger.error("ConversationsRepository::update", e);
		}
	}

	@Override
	public Conversations findById(Long id) {
		try {
			return jdbcTemplate.queryForObject(
                "SELECT * FROM conversations WHERE id_usuario = ?",
                new Object[]{ id }, new ConversationsRowMapper());
		} catch(DataAccessException e) {
			return null;
		}
	}

	@Override
	public void delete(Conversations record) {
		try {
			final String sql = "DELETE FROM conversations WHERE id_conversation = ?";
			jdbcTemplate.update(sql, new Object[] {
					record.getId_conversation()
			});
		} catch(DataAccessException e) {
			logger.error("ConversationsRepository::delete", e);
		}
	}
	
	class ConversationsRowMapper implements RowMapper<Conversations> {
	    public Conversations mapRow(ResultSet rs, int rowNum) throws SQLException {
	        return new Conversations(
	        		rs.getLong("id_conversation"),
	        		usersRepository.findById(rs.getLong("user_origin")),
	        		usersRepository.findById(rs.getLong("user_target")),
	        		rs.getDate("last_message")
	        		);
	    }
	}
	
}
