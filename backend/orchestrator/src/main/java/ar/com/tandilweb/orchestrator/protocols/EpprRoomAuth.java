package ar.com.tandilweb.orchestrator.protocols;

import java.util.UUID;

import org.springframework.stereotype.Component;

import ar.com.tandilweb.exchange.roomAuth.TokenUpdate;

@Component
public class EpprRoomAuth {
	
	// TODO:
	public TokenUpdate getTokenUpdateSchema() {
		TokenUpdate out = new TokenUpdate();
		out.securityToken = UUID.randomUUID().toString();
		return out;
	}

}
