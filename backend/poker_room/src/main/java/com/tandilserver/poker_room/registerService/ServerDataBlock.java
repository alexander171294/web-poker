package com.tandilserver.poker_room.registerService;

import org.springframework.stereotype.Component;

import com.tandilserver.poker_intercom.handshake.ServerOperation;

@Component
public class ServerDataBlock {
	
	protected ServerOperation srvInfo = new ServerOperation();

	public ServerOperation getSrvInfo() {
		return srvInfo;
	}

	public void setSrvInfo(ServerOperation srvInfo) {
		this.srvInfo = srvInfo;
	}

}
