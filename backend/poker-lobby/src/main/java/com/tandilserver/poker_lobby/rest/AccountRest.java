package com.tandilserver.poker_lobby.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tandilserver.poker_intercom.genericResponse.Response;
import com.tandilserver.poker_intercom.genericResponse.StatusCodes;
import com.tandilserver.poker_lobby.dataBase.domain.Users;
import com.tandilserver.poker_lobby.dataBase.repository.UsersRepository;
import com.tandilserver.poker_lobby.rest.dto.PasswordData;
import com.tandilserver.poker_lobby.rest.dto.UserData;

@RestController
@RequestMapping("/accountRest")
public class AccountRest { // For user data operations, coins, change password, basic information, etc.
	
	@Autowired
	UsersRepository usersRepository;

	@RequestMapping(path="/data", method=RequestMethod.GET)
	public UserData data(@RequestHeader("jwtUserOrigin") Users userRequester) {
		UserData uD = new UserData();
		uD.fichas = userRequester.getCoins();
		uD.nick = userRequester.getNick();
		return uD;
	}
	
	@RequestMapping(path="/changePassword", method=RequestMethod.GET)
	public Response changePassword(@RequestHeader("jwtUserOrigin") Users userRequester, @RequestBody PasswordData password) {
		Response out = new Response();
		try {
			if(!userRequester.getPassword().equals(password.oldPassword)) new Exception("Invalid old password");
			if(password.newPassword == null || password.newPassword.length() < 8) new Exception("Password must be more than 8 chars");
			if(password.newPassword.length() > 16) new Exception("Password must be less than 16 chars");
			userRequester.setPassword(password.newPassword);
			usersRepository.update(userRequester);
		} catch(Exception e) {
			out.statusCode = StatusCodes.ERR;
			out.message = e.getMessage();
		}
		return out;
	}
	
	

}
