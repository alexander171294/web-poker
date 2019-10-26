package ar.com.tandilweb.room.orchestratorBridge;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.com.tandilweb.exchange.Schema;
import ar.com.tandilweb.exchange.roomAuth.Handshake;
import ar.com.tandilweb.room.handlers.GameHandler;
import ar.com.tandilweb.room.handlers.RoomHandler;
import ar.com.tandilweb.room.orchestratorBridge.processors.BackwardValidationProcessor;
import ar.com.tandilweb.room.orchestratorBridge.processors.RoomAuthProcessor;
import ar.com.tandilweb.room.orchestratorBridge.processors.ServerRecordingProcessor;
import ar.com.tandilweb.room.protocols.EpprRoomAuth;

@Component
public class OrchestratorThread implements Runnable, ApplicationListener<ContextRefreshedEvent> {

	public static Logger logger = LoggerFactory.getLogger(OrchestratorThread.class);
	private Thread thread;

	@Value("${act.room.orchestrator.remoteListenerPort}")
	private volatile int remoteListenerPort;

	@Value("${act.room.orchestrator.remoteAddr}")
	private volatile String remoteAddr;

	BufferedReader socketBufferReader;
	private PrintWriter socketBufferOutput;

	private Socket socket;
	private boolean scanning;

	@Autowired
	private EpprRoomAuth roomAuthProto;

	@Autowired
	private RoomHandler roomHandler;
	
	@Autowired
	private RoomAuthProcessor roomAuthProcessor;
	
	@Autowired
	private BackwardValidationProcessor backwardValidationProcessor;
	
	@Autowired
	private ServerRecordingProcessor serverRecordingProcessor;

	@Value("${act.room.cfgFileSave}")
	private String cfgFileSave;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (this.thread == null) {
			this.thread = new Thread(this);
			this.thread.start();
			logger.debug("RegisterService Thread created.");
		}
	}

	@Override
	public void run() {
		try {
			createSocketConneciton();
		} catch (IOException e) {
			logger.error("Connection IO Exception, ", e);
		}
		try {
			main();
		} catch (Exception e) {
			logger.error("Socket read Error", e);
		} finally {
			try {
				socketBufferOutput.close();
				socketBufferReader.close();
				socket.close();
				logger.error("Connection Closed");
			} catch (IOException e) {
				logger.error("Loop close error", e);
			}
		}
	}

	private void main() {
		try {
			scanning = true;
			File configuration = new File(cfgFileSave + File.separator + "lastHandshake.json");
			Handshake hs;
			if (configuration.exists()) {
				ObjectMapper objectMapper = new ObjectMapper();
				hs = objectMapper.readValue(configuration, Handshake.class);
				roomHandler.setRoomID(hs.serverID);
			} else {
				hs = roomAuthProto.getHandshakeSchema();
			}
			ObjectMapper om = new ObjectMapper();
			sendDataToServer(om.writeValueAsString(hs));
			String message;
			do {
				message = socketBufferReader.readLine();
				if (message != null) {
					processIncommingMessage(message);
				}
			} while (scanning && message != null);
			logger.debug("Finish thread by: "
					+ (message == null ? "Null message received (connection lost?)" : "scanning off"));
		} catch (IOException e) {
			logger.error("MAIN THREAD IO EXCEPTION: ", e);
		}
	}

	private void createSocketConneciton() throws UnknownHostException, IOException {
		socket = new Socket(remoteAddr, this.remoteListenerPort);
		socketBufferReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		socketBufferOutput = new PrintWriter(socket.getOutputStream());
		// set streams:
		roomAuthProcessor.setSocketBufferOutput(socketBufferOutput);
		backwardValidationProcessor.setSocketBufferOutput(socketBufferOutput);
	}

	public void sendDataToServer(String data) {
		socketBufferOutput.println(data);
		socketBufferOutput.flush();
	}

	private void processIncommingMessage(String message) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Schema inputSchema = om.readValue(message, Schema.class);
		processSchema(inputSchema, message);
	}

	private void processSchema(Schema schema, String schemaBody) throws IOException {
		if ("eppr/room-auth".equals(schema.namespace)) {
			roomAuthProcessorSelector(schema, schemaBody);
		} else if ("eppr/backward-validation".equals(schema.namespace)) {
			backwardValidationProcessorSelector(schema, schemaBody);
		} else if ("eppr/server-recording".equals(schema.namespace)) {
			serverRecordingProcessorSelector(schema, schemaBody);
		} else {
			logger.debug("Unexpected namespace received", schema);
		}
	}
	
	private void roomAuthProcessorSelector(Schema schema, String schemaBody) throws JsonProcessingException {
		switch (schema.schema) {
		case "signup":
			roomAuthProcessor.processSignupSchema(schemaBody);
			break;
		case "signupResponse":
			roomAuthProcessor.processSignupResponseSchema(schemaBody);
			break;
		case "retry":
			roomAuthProcessor.processRetrySchema(schemaBody);
			break;
		case "rejected":
			roomAuthProcessor.processRejectedSchema(schemaBody);
			break;
		case "exceeded":
			roomAuthProcessor.processExceededSchema(schemaBody);
			break;
		case "tokenUpdate":
			roomAuthProcessor.processTokenUpdate(schemaBody);
			break;
		case "busy":
			roomAuthProcessor.processBusySchema(schemaBody);
			break;
		default:
			logger.debug("Schema not recognized: " + schema.schema + " for namespace " + schema.namespace);
			break;
		}
	}
	
	private void backwardValidationProcessorSelector(Schema schema, String schemaBody) throws JsonProcessingException {
		switch (schema.schema) {
		case "dataChallenge":
			backwardValidationProcessor.processDataChallengeSchema(schemaBody);
			break;
		case "invalid":
			backwardValidationProcessor.processInvalidSchema(schemaBody);
			break;
		case "unknown":
			backwardValidationProcessor.processUnknownSchema(schemaBody);
			break;
		default:
			logger.debug("Schema not recognized: " + schema.schema + " for namespace " + schema.namespace);
			break;
		}
	}
	
	private void serverRecordingProcessorSelector(Schema schema, String schemaBody) throws JsonProcessingException {
		switch(schema.schema) {
		case "successDeposit":
			serverRecordingProcessor.processSuccessDeposit(schemaBody);
			break;
		case "invalidDeposit":
			serverRecordingProcessor.processInvalidDeposit(schemaBody);
			break;
		default:
			logger.debug("Schema not recognized: " + schema.schema + " for namespace " + schema.namespace);
			break;
		}
	}
}
