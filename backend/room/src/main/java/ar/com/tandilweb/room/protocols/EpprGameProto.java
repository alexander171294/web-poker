package ar.com.tandilweb.room.protocols;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.accessing.Announcement;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.accessing.DefinePosition;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.accessing.Ingress;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.accessing.RejectFullyfied;

@Component
public class EpprGameProto {
	
	public static Logger logger = LoggerFactory.getLogger(EpprGameProto.class);
	
	public DefinePosition getDefinePositionSchema(List<Integer> freeSpaces) {
		return null;
	}

	public RejectFullyfied getRejectFullyfiedSchema() {
		return null;
	}
	
	public Ingress getIngressSchema() {
		return null;
	}
	
	public Announcement getAnnouncementSchema() {
		return null;
	}
}
