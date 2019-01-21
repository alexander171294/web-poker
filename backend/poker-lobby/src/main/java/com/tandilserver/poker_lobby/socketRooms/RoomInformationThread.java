package com.tandilserver.poker_lobby.socketRooms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tandilserver.poker_intercom.handshake.AuthorizationBearer;
import com.tandilserver.poker_intercom.handshake.ServerInfo;
import com.tandilserver.poker_lobby.dataBase.domain.Rooms;
import com.tandilserver.poker_lobby.socketRooms.services.RoomService;

@Component
@Scope("prototype")
public class RoomInformationThread implements Runnable {

	protected Socket socket;
	protected PrintWriter output = null;
	protected BufferedReader input = null;
	protected static Logger logger = LoggerFactory.getLogger(RoomInformationThread.class);
	protected String name;
	protected StepHandshake stepHandshake = StepHandshake.OFFLINE;
	
	private Long id_server;
	private String server_identity_hash;
	
	@Autowired
	protected RoomService roomService;

	public RoomInformationThread() {
		logger.debug("Socket connected - New Thread");
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setSocket(Socket clientSocket) {
		this.socket = clientSocket;
	}

	@Override
	public void run() {
		try {
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			logger.error("IO error in server thread", e);
			return;
		}
		try {
			String line = input.readLine();
			while ((line != null) && !line.equalsIgnoreCase("QUIT")) {
				onMessageReceived(line);
				line = input.readLine();
			}
		} catch (IOException e) {
			String name = this.name;
			logger.info("IO Error/ Client " + name + " terminated abruptly", e);
		} catch (NullPointerException e) {
			String name = this.name;
			logger.info("Client " + name + " closed", e);
		} finally {
			closeConnection();
		}
	}

	protected void sendToClient(String data) {
		output.println(data);
		output.flush();
	}

	protected void closeConnection() {
		try {
			logger.debug("Connection closing...");
			if (input != null) {
				input.close();
				logger.debug("Socket Input Stream Closed");
			}
			if (output != null) {
				output.close();
				logger.debug("Socket Out Closed");
			}
			if (socket != null) {
				socket.close();
				logger.debug("Socket Closed");
			}
		} catch (IOException ioe) {
			logger.error("Socket Close Error", ioe);
		}
	}

	protected void onMessageReceived(String line) {
		switch (this.stepHandshake) {
			case OFFLINE:
				checkServerInformation(line);
				break;
			case AUTHORIZATION_REQUIRED:
				validateAuthorization(line);
				break;
			case AUTHORIZED:
				postAuthorizedUpdater(line);
				break;
			default:
				logger.debug("Unknown Step");
				break;	
		}
	}
	
	protected void checkServerInformation(String data) {
		ObjectMapper oM = new ObjectMapper();
		ServerInfo srvInfo;
		try {
			srvInfo = oM.readValue(data, ServerInfo.class);
			if(srvInfo != null) {
				if (srvInfo.officialServer) {
					// TODO: validate if official server and add config to enable/disable unofficial server supports.
				} else {
					Rooms room = roomService.addIfNotExistsRoomServer(srvInfo);
					AuthorizationBearer auth = new AuthorizationBearer();
					auth.id_server = 0; // TODO: rework insert function to get id of last insert record.
					auth.expiration = new Date(); // TODO: set expiration system
					if(room.isNewItem()) {
						this.stepHandshake = StepHandshake.AUTHORIZED;
						// send response:
						auth.server_identity_hash = room.getServerIdentityHash();
						this.server_identity_hash = auth.server_identity_hash;
						auth.valid = true;
						sendToClient(oM.writeValueAsString(auth));
					} else {
						this.stepHandshake = StepHandshake.AUTHORIZATION_REQUIRED;
						// send response
						auth.server_identity_hash = "";
						auth.valid = false;
						id_server = room.getId_server();
						sendToClient(oM.writeValueAsString(auth));
					}
				}
			} else {
				logger.error("Error of server info");
			}
		} catch (IOException e) {
			logger.error("Error of mapping");
		}
	}
	
	protected void validateAuthorization(String data) {
		ObjectMapper oM = new ObjectMapper();
		AuthorizationBearer auth;
		try {
			auth = oM.readValue(data, AuthorizationBearer.class);
			if(roomService.validateOldHash(id_server, auth.server_identity_hash)) {
				Rooms room = roomService.updateRoomHash(id_server, UUID.randomUUID().toString());
				auth.id_server = room.getId_server();
				auth.server_identity_hash = room.getServerIdentityHash();
				this.server_identity_hash = auth.server_identity_hash;
				auth.expiration = new Date(); // TODO: set expiration system
				auth.valid = true;
				this.stepHandshake = StepHandshake.AUTHORIZED;
			} else {
				auth.valid = false;
			}
			sendToClient(oM.writeValueAsString(auth));
		} catch (IOException e) {
			logger.error("Error validating authorization");
		}
	}
	
	protected void postAuthorizedUpdater(String data) {
		ObjectMapper oM = new ObjectMapper();
		ServerInfo srvInfo;
		try {
			srvInfo = oM.readValue(data, ServerInfo.class);
			if(srvInfo != null) {
				roomService.updateRoomInfo(id_server, srvInfo);
			}
		} catch (IOException e) {
			logger.error("Error of mapping postAuthorized");
		}
	}

}
