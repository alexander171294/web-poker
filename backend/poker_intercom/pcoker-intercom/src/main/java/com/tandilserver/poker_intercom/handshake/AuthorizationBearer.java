package com.tandilserver.poker_intercom.handshake;

import java.util.Date;

public class AuthorizationBearer {
	public long id_server;
	public String server_identity_hash;
	public Date expiration;
	public boolean valid;
}
