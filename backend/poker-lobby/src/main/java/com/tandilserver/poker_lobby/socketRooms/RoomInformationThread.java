package com.tandilserver.poker_lobby.socketRooms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tandilserver.poker_intercom.handshake.ServerInfo;

@Component
@Scope("prototype")
public class RoomInformationThread implements Runnable {

	protected Socket socket;
	protected PrintWriter output = null;
	protected BufferedReader input = null;
	protected static Logger logger = LoggerFactory.getLogger(RoomInformationThread.class);
	protected String name;
	protected StepHandshake stepHandshake = StepHandshake.OFFLINE;

	public RoomInformationThread(Socket clientSocket, String name) {
		logger.debug("Socket connected - New Thread");
		this.name = name;
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
			case AUTHORIZATION:
				validateAuthorization(line);
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
				
			} else {
				logger.error("Error of server info");
			}
		} catch (IOException e) {
			logger.error("Error of mapping");
		}
	}
	
	protected void validateAuthorization(String data) {
		
	}

}
