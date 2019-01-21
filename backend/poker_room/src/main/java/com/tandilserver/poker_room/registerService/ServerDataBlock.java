package com.tandilserver.poker_room.registerService;

import org.springframework.stereotype.Component;

import com.tandilserver.poker_intercom.handshake.ServerInfo;

@Component
public class ServerDataBlock {
	
	protected ServerInfo srvInfo = new ServerInfo();

	public ServerInfo getSrvInfo() {
		return srvInfo;
	}

	public void setSrvInfo(ServerInfo srvInfo) {
		this.srvInfo = srvInfo;
	}

}
