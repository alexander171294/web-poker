package com.tandilserver.poker_lobby.socketRooms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tandilserver.poker_intercom.handshake.UserDataVerified;
import com.tandilserver.poker_intercom.handshake.UserDataVerifier;
import com.tandilserver.poker_lobby.dataBase.domain.Users;
import com.tandilserver.poker_lobby.dataBase.repository.UsersRepository;

@Service
public class UserService {
	
	@Autowired
	private UsersRepository userRepository;
	
	public UserDataVerified validateUserInformation(UserDataVerifier udv) {
		UserDataVerified out = new UserDataVerified();
		out.valid = false;
		out.userSessionUUID = udv.userSessionUUID;
		
		//udv.signature // TODO: VALIDAR JWT;
		Users user = userRepository.findById(udv.id_usuario);
		if (user != null) {
			out.valid = true;
			out.coins = user.getCoins();
		}
		return out;
	}

}
