package com.tandilserver.poker_lobby.dataBase.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tandilserver.poker_lobby.dataBase.BaseRepository;
import com.tandilserver.poker_lobby.dataBase.domain.Users;;

@Repository
public class UsersRepository extends BaseRepository<Users, Long> {
	
	public static Logger logger = LoggerFactory.getLogger(UsersRepository.class);

	@Override
	public Users create(Users record) {
		try {
			final String sql = "INSERT INTO users"
					+ "(nick, email, password, fecha_registro, ultima_actividad, fichas, id_user_recommend, hashSignature)"
					+ " VALUES(?,?,?,?,?,?,?)";
			jdbcTemplate.update(sql, new Object[] {
					record.getNick(),
					record.getEmail(),
					record.getPassword(),
					record.getSignup_date(),
					record.getLast_activity(),
					record.getCoins(),
					record.getId_user_recommend(),
					record.getHashSignature()
					});
			return record;
		} catch(DataAccessException e) {
			logger.error("UsersRepository::create", e);
			return null;
		}
	}

	@Override
	public void update(Users record) {
		try {
			final String sql = "UPDATE users SET "
					+ "nick = ?, email = ?, password, signup_date, last_activity, coins, hashSignature, id_user_recommend WHERE id_usuario = ?";
			jdbcTemplate.update(sql, new Object[] {
					record.getNick(),
					record.getEmail(),
					record.getPassword(),
					record.getSignup_date(),
					record.getLast_activity(),
					record.getCoins(),
					record.getHashSignature(),
					record.getId_user_recommend(),
					record.getId_usuario()
			});
		} catch(DataAccessException e) {
			logger.error("UsersRepository::update", e);
		}
	}

	@Override
	public Users findById(Long id) {
		try {
			return jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE id_usuario = ?",
                new Object[]{ id }, new UsersRowMapper());
		} catch(DataAccessException e) {
			return null;
		}
	}
	
	public Users findByEmail(String email) {
		try {
			return jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE email = ?",
                new Object[]{ email }, new UsersRowMapper());
		} catch(DataAccessException e) {
			return null;
		}
	}
	
	public Users findByNick(String nick) {
		try {
			return jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE nick = ?",
                new Object[]{ nick }, new UsersRowMapper());
		} catch(DataAccessException e) {
			return null;
		}
	}
	
	class UsersRowMapper implements RowMapper<Users>
	{
	    public Users mapRow(ResultSet rs, int rowNum) throws SQLException {
	        return new Users(
	        		rs.getLong("id_usuario"),
	        		rs.getString("nick"),
	        		rs.getString("email"),
	        		rs.getString("password"),
	        		rs.getDate("signup_date"),
	        		rs.getDate("last_activity"),
	        		rs.getLong("coins"),
	        		rs.getString("hashSignature"),
	        		rs.getLong("id_user_recommend")
	        		);
	    }
	}
	
}
