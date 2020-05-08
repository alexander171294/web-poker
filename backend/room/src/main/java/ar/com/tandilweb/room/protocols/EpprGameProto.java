package ar.com.tandilweb.room.protocols;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.accessing.Announcement;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.accessing.DefinePosition;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.accessing.Ingress;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.accessing.RejectFullyfied;
import ar.com.tandilweb.exchange.gameProtocol.texasHoldem.accessing.RejectedPosition;
import ar.com.tandilweb.room_int.handlers.dto.UserData;

@Component
public class EpprGameProto {
	
	public static Logger logger = LoggerFactory.getLogger(EpprGameProto.class);
	
	public DefinePosition getDefinePositionSchema(List<Integer> freeSpaces) {
		DefinePosition out = new DefinePosition();
		out.positions = freeSpaces;
		return out;
	}

	public RejectFullyfied getRejectFullyfiedSchema() {
		return new RejectFullyfied();
	}
	
	public Ingress getIngressSchema(UserData userData, int position) {
		Ingress out = new Ingress();
		out.chips = userData.chips;
		out.position = position;
		return out;
	}
	
	public Announcement getAnnouncementSchema(UserData userData, int position) {
		Announcement out = new Announcement();
		out.position = position;
		out.chips = userData.chips;
		out.avatar = userData.dataBlock.getPhoto();
		out.user = userData.dataBlock.getNick_name();
		out.userID = userData.dataBlock.getId_user();
		return out;
	}
	
	public RejectedPosition getRejectedPositionSchema(List<Integer> freeSpaces) {
		RejectedPosition position = new RejectedPosition();
		position.positions = freeSpaces;
		return position;
	}
}
