package com.tandilserver.poker_lobby.dataBase.repository;

import com.tandilserver.poker_lobby.dataBase.domain.Users;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<Users, Long> {
	Users findByEmail(String email);
	Users findByNick(String nick);
}
