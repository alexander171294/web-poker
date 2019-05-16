package ar.com.tandilweb.ApiServer.persistence.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import ar.com.tandilweb.ApiServer.persistence.BaseRepository;
import ar.com.tandilweb.ApiServer.persistence.domain.Users;

@Repository
public class UsersRepository extends BaseRepository<Users, Long> {
	
	public static Logger logger = LoggerFactory.getLogger(UsersRepository.class);

	@Override
	public Users create(Users record) {
		try {
			final String sql = "INSERT INTO users "
					+ "(nick_name, email, password, chips, photo, badLogins) "
					+ "VALUES(?,?,?,?,?,?)";
			KeyHolder holder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, record.getNick_name());
					ps.setString(2, record.getEmail());
					ps.setString(3, record.getPassword());
					ps.setLong(4, record.getChips());
					ps.setString(5, record.getPhoto());
					ps.setInt(6, record.getBadLogins());
					return ps;
				}
			}, holder);
			record.setId_user(holder.getKey().longValue());
			return record;
		} catch(DataAccessException e) {
			logger.error("UsersRepository::create", e);
			return null;
		}
	}

	@Override
	public void update(Users record) {
		try {
			final String sql = "UPDATE users SET email = ?, password = ?, chips = ?, photo = ?, badLogins = ? WHERE id_user = ?";
			jdbcTemplate.update(sql, new Object[] {
					record.getEmail(),
					record.getPassword(),
					record.getChips(),
					record.getPhoto(),
					record.getBadLogins(),
					record.getId_user()
			});
		} catch(DataAccessException e) {
			logger.error("UsersRepository::update", e);
		}
	}

	@Override
	public Users findById(Long id) {
		try {
			return jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE id_user = ?",
                new Object[]{id}, new UsersRowMapper());
		} catch(DataAccessException e) {
			return null;
		}
	}
	
	public List<Users> getFromFriendshipsFor(long origin_target) {
		try {
			// FIXME: imporve this queries:
			List<Users> users = jdbcTemplate.query(
	                "SELECT * FROM users as U LEFT JOIN friendships AS F ON F.id_user_target = ? AND F.accepted = true WHERE U.id_user = F.id_user_origin",
	                new Object[]{ origin_target }, new UsersRowMapper());
			users.addAll(jdbcTemplate.query(
	                "SELECT * FROM users as U LEFT JOIN friendships AS F ON F.id_user_origin = ? AND F.accepted = true WHERE U.id_user = F.id_user_target",
	                new Object[]{ origin_target }, new UsersRowMapper()));
			return users;
		} catch(DataAccessException e) {
			return null;
		}
	}
	
	public List<Users> getFromFriendshipsPendingsFor(long origin_target) {
		try {
			// FIXME: imporve this queries:
			List<Users> users = jdbcTemplate.query(
	                "SELECT * FROM users as U LEFT JOIN friendships AS F ON F.id_user_target = ? AND F.accepted = false WHERE U.id_user = F.id_user_origin",
	                new Object[]{ origin_target }, new UsersRowMapper());
			users.addAll(jdbcTemplate.query(
	                "SELECT * FROM users as U LEFT JOIN friendships AS F ON F.id_user_origin = ? AND F.accepted = false WHERE U.id_user = F.id_user_target",
	                new Object[]{ origin_target }, new UsersRowMapper()));
			return users;
		} catch(DataAccessException e) {
			return null;
		}
	}
	
	public boolean checkEmailUser(String email, String nick_name) {
		try {
			int total = jdbcTemplate.queryForObject(
                "SELECT count(id_user) FROM users WHERE email = ? OR nick_name = ?",
                new Object[]{email, nick_name}, Integer.class);
			return total > 0;
		} catch(DataAccessException e) {
			return false;
		}
	}
	
	public Users findByEmailOrUser(String umail) {
		try {
			return jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE email = ? OR nick_name = ?",
                new Object[]{umail, umail}, new UsersRowMapper());
		} catch(DataAccessException e) {
			return null;
		}
	}
	
	class UsersRowMapper implements RowMapper<Users> {
		public Users mapRow(ResultSet rs, int rowNum) throws SQLException {
	        return new Users(
	        		rs.getLong("id_user"),
	        		rs.getString("nick_name"),
	        		rs.getString("email"),
	        		rs.getString("password"),
	        		rs.getLong("chips"),
	        		rs.getString("photo"),
	        		rs.getShort("badLogins")
	        		);
	    }
	}
	
}
