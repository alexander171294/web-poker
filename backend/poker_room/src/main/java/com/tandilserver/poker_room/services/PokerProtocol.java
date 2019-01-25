package com.tandilserver.poker_room.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Service;

import com.tandilserver.poker_room.controllers.dto.out.RequestDeposit;

@Service
@PropertySources(value = {@PropertySource("classpath:roomConfigurations.properties")})
public class PokerProtocol {
	
	@Autowired
	private MessageRouterService msgRouterServ;
	
	@Autowired
	private UserService usrSrv;
	
	@Value("${room.register.coinsRequired4Sit}")
	private Long coinsRequired4Sit;
	
	public void sendRequestDeposit(String userUUID, Long coins) {
		RequestDeposit rd = new RequestDeposit();
		rd.actualCoins = coins;
		rd.required4Sit = this.coinsRequired4Sit;
		msgRouterServ.sendToClientInterceptor(usrSrv.getMessageRouterUUID(userUUID), "/requestDeposit", rd);
	}

}
