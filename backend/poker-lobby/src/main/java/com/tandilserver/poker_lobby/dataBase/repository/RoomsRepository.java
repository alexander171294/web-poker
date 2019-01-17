package com.tandilserver.poker_lobby.dataBase.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;

import com.tandilserver.poker_lobby.dataBase.BaseRepository;
import com.tandilserver.poker_lobby.dataBase.customTypes.LimitTypes;
import com.tandilserver.poker_lobby.dataBase.customTypes.ServerTypes;
import com.tandilserver.poker_lobby.dataBase.domain.Rooms;

public class RoomsRepository extends BaseRepository<Rooms, Long>{
	
	public static Logger logger = LoggerFactory.getLogger(RoomsRepository.class);

	@Override
	public Rooms create(Rooms record) {
		try {
			final String sql = "INSERT INTO rooms"
					+ "(ip, port, server_name, players, blind, min_bet, server_type, limit_bet)"
					+ " VALUES(?,?,?,?,?,?,?,?)";
			jdbcTemplate.update(sql, new Object[] {
					record.getIp(),
					record.getPort(),
					record.getServer_name(),
					record.getPlayers(),
					record.getBlind(),
					record.getMin_bet(),
					record.getServer_type().ordinal(),
					record.getLimit_bet().ordinal()
					});
			return record;
		} catch(DataAccessException e) {
			logger.error("RoomsRepository::create", e);
			return null;
		}
	}

	@Override
	public void update(Rooms record) {
		try {
			final String sql = "UPDATE rooms SET "
					+ "port = ?, server_name = ?, players = ?, blind = ?, min_bet = ?, server_type = ?, limit_bet = ? WHERE id_server = ?";
			jdbcTemplate.update(sql, new Object[] {
					record.getPort(),
					record.getServer_name(),
					record.getPlayers(),
					record.getBlind(),
					record.getMin_bet(),
					record.getServer_type(),
					record.getLimit_bet(),
					record.getId_server()
			});
		} catch(DataAccessException e) {
			logger.error("RoomsRepository::update", e);
		}
	}

	@Override
	public Rooms findById(Long id) {
		try {
			return jdbcTemplate.queryForObject(
                "SELECT * FROM rooms WHERE id_server = ?",
                new Object[]{ id }, new RoomsRowMapper());
		} catch(DataAccessException e) {
			return null;
		}
	}

	@Override
	public void delete(Rooms record) {
		try {
			final String sql = "DELETE FROM rooms WHERE id_server = ?";
			jdbcTemplate.update(sql, new Object[] {
					record.getId_server()
			});
		} catch(DataAccessException e) {
			logger.error("RoomsRepository::delete", e);
		}
	}
	
	class RoomsRowMapper implements RowMapper<Rooms> {
	    public Rooms mapRow(ResultSet rs, int rowNum) throws SQLException {
	        return new Rooms(
	        		rs.getLong("id_server"),
	        		rs.getString("ip"),
	        		rs.getInt("port"),
	        		rs.getString("server_name"),
	        		rs.getInt("players"),
	        		rs.getLong("blind"),
	        		rs.getLong("min_bet"),
	        		ServerTypes.values()[rs.getInt("server_type")],
	        		LimitTypes.values()[rs.getInt("limit_bet")]
	        		);
	    }
	}

}
