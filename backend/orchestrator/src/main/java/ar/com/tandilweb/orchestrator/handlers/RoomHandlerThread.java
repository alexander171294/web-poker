package ar.com.tandilweb.orchestrator.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.com.tandilweb.exchange.Schema;
import ar.com.tandilweb.exchange.roomAuth.Handshake;
import ar.com.tandilweb.orchestrator.processors.BackwardValidationProcessor;
import ar.com.tandilweb.orchestrator.processors.RoomAuthProcessor;
import ar.com.tandilweb.orchestrator.processors.ServerRecordingProcessor;

@Component
@Scope("prototype")
public class RoomHandlerThread implements Runnable {
	
	protected static Logger logger = LoggerFactory.getLogger(RoomHandlerThread.class);

	protected Socket socket;
	protected PrintWriter output = null;
	protected BufferedReader input = null;
	
	protected String name;
	
	protected Handshake handshakeSchema;
	protected boolean logged;
	
	@Autowired
	RoomAuthProcessor roomAuthProcessor;
	
	@Autowired
	BackwardValidationProcessor backwardValidationProcessor;
	
	@Autowired
	ServerRecordingProcessor serverRecordingProcessor;

	public RoomHandlerThread() {
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
		logger.debug("Room Handler Thread Started");
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
				processIncommingMessage(line);
				line = input.readLine();
			}
		} catch (IOException e) {
			String name = this.name;
			logger.info("IO Error/ Client " + name + " terminated abruptly", e);
		} catch (NullPointerException e) {
			String name = this.name;
			logger.info("Client " + name + " closed", e);
		}
		closeConnection();
	}

	public void sendToClient(String data) {
		output.println(data);
		output.flush();
	}
	
	public void setHandshakeSchema(Handshake hs) {
		this.handshakeSchema = hs;
	}

	protected void closeConnection() {
		try {
			logger.debug("Connection closing...");
			this.roomAuthProcessor.goOffline(this.handshakeSchema.serverID);
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

	private void processIncommingMessage(String message) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Schema inputSchema = om.readValue(message, Schema.class);
		processSchema(inputSchema, message);
	}

	protected void processSchema(Schema schema, String schemaBody) throws JsonParseException, JsonMappingException, IOException {
		logger.debug(schemaBody);
		ObjectMapper om = new ObjectMapper();
		// TODO: validate room ip against Whitelist.
		if ("eppr/room-auth".equals(schema.namespace)) {
			switch (schema.schema) {
			case "handshake":
				this.logged = this.roomAuthProcessor.processHandshakeSchema(schemaBody, this);
				return;
			case "signupData":
				this.logged = this.roomAuthProcessor.processSignupDataSchema(schemaBody, this.handshakeSchema, this);
				return;
			default:
				logger.debug("Schema not recognized: " + schema.schema + " for namespace " + schema.namespace);
				return;
			}
		}
		if ("eppr/backward-validation".equals(schema.namespace)) {
			switch (schema.schema) {
			case "challengeValidation":
				this.sendToClient(om.writeValueAsString(this.backwardValidationProcessor.challengeValidation(schemaBody, this.handshakeSchema)));
				return;
			default:
				logger.debug("Schema not recognized: " + schema.schema + " for namespace " + schema.namespace);
				return;
			}
		}
		if ("eppr/server-recording".equals(schema.namespace)) {
			switch(schema.schema) {
			case "deposit" :
				this.sendToClient(om.writeValueAsString(this.serverRecordingProcessor.deposit(schemaBody, this.handshakeSchema)));
				return;
			case "userEnd" :
				this.serverRecordingProcessor.userEnd(schemaBody, this.handshakeSchema);
				return;
			default: 
				logger.debug("Schema not recognized: " + schema.schema + " for namespace " + schema.namespace);
				return;
			}
		}
		
		logger.debug("Namespace not recognized: " + schema.namespace);
	}

}
