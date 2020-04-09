package ar.com.tandilweb.persistence.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import ar.com.tandilweb.persistence.BaseRepository;
import ar.com.tandilweb.persistence.domain.UsersInRooms;

public class UsersInRoomsRepository extends BaseRepository<UsersInRooms, Long> {

	@Override
	public UsersInRooms create(final UsersInRooms record) {
		try {
			final String sql = "INSERT INTO users_in_rooms "
					+ "(id_user, id_room, registered, position) "
					+ "VALUES(?,?,?,?)";
			KeyHolder holder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setLong(1, record.getId_user());
					ps.setLong(2, record.getId_room());
					ps.setDate(3, new java.sql.Date(record.getRegistered().getTime()));
					ps.setLong(4, record.getPosition());
					return ps;
				}
			}, holder);
			//record.setId_room(holder.getKey().longValue());
			return record;
		} catch(DataAccessException e) {
			// update last action ?
			
			return null;
		}
	}

	@Override
	public void update(UsersInRooms record) {
		
	}

	@Override
	public UsersInRooms findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void removeAll(Long roomID) {
		final String sql = "DELETE FROM users_in_rooms WHERE id_room = ?";
		jdbcTemplate.update(sql, new Object[]{roomID});
	}
	
	public long count(Long roomID) {
		final String sql = "SELECT count(id_user) FROM users_in_rooms WHERE id_room = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { roomID }, Long.class);
	}
	
}
