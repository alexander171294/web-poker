package com.tandilserver.poker_lobby.dataBase.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tandilserver.poker_lobby.dataBase.BaseRepository;
import com.tandilserver.poker_lobby.dataBase.domain.Messages;

@Repository
public class MessagesRepository extends BaseRepository<Messages, Long>{
	
	public static Logger logger = LoggerFactory.getLogger(MessagesRepository.class);

	@Override
	public Messages create(Messages record) {
		try {
			final String sql = "INSERT INTO messages"
					+ "(id_conversation, id_user_sender, date_sended, readed)"
					+ " VALUES(?,?,?,?)";
			jdbcTemplate.update(sql, new Object[] {
					record.getId_conversation(),
					record.getSender(),
					record.getDate_sended(),
					record.getReaded(),
					});
			return record;
		} catch(DataAccessException e) {
			logger.error("MessagesRepository::create", e);
			return null;
		}
	}

	@Override
	public void update(Messages record) {
		try {
			final String sql = "UPDATE messages SET "
					+ "readed = ? WHERE id_message = ?";
			jdbcTemplate.update(sql, new Object[] {
					record.getReaded(),
					record.getId_message()
			});
		} catch(DataAccessException e) {
			logger.error("MessagesRepository::update", e);
		}
	}

	@Override
	public Messages findById(Long id) {
		try {
			return jdbcTemplate.queryForObject(
                "SELECT * FROM messages WHERE id_message = ?",
                new Object[]{ id }, new MessagesRowMapper());
		} catch(DataAccessException e) {
			return null;
		}
	}
	
	@Override
	public void delete(Messages record) {
		try {
			final String sql = "DELETE FROM messages WHERE id_message = ?";
			jdbcTemplate.update(sql, new Object[] {
					record.getId_message()
			});
		} catch(DataAccessException e) {
			logger.error("MessagesRepository::delete", e);
		}
	}
	
	class MessagesRowMapper implements RowMapper<Messages> {
	    public Messages mapRow(ResultSet rs, int rowNum) throws SQLException {
	        return new Messages(
	        		rs.getLong("id_message"),
	        		rs.getLong("id_conversation"),
	        		rs.getLong("id_user_sender"),
	        		rs.getDate("date_sended"),
	        		rs.getBoolean("readed")
	        		);
	    }
	}

}
