package com.tandilserver.poker_lobby.rest;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tandilserver.poker_lobby.dataBase.domain.Users;
import com.tandilserver.poker_lobby.rest.dto.UserData;

@RestController
@RequestMapping("/accountRest")
public class AccountRest { // For user data operations, coins, change password, basic information, etc.

	@RequestMapping(path="/data", method=RequestMethod.GET)
	public UserData data(@RequestHeader("jwtUserOrigin") Users userRequester) {
		UserData uD = new UserData();
		uD.fichas = userRequester.getFichas();
		uD.nick = userRequester.getNick();
		return uD;
	}

}
