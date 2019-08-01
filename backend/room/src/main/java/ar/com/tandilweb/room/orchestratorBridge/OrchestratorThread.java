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
import ar.com.tandilweb.exchange.roomAuth.SignupData;
import ar.com.tandilweb.exchange.roomAuth.SignupResponse;
import ar.com.tandilweb.exchange.roomAuth.TokenUpdate;
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
	
	@Value("${act.room.cfgFileSave}")
	private String cfgFileSave;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(this.thread == null) {
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
        try{
        	main();
        } catch(Exception e){
            logger.error("Socket read Error", e);
        } finally{
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
			if(configuration.exists()) {
				ObjectMapper objectMapper = new ObjectMapper();
				hs = objectMapper.readValue(configuration, Handshake.class);
			} else {
				hs = roomAuthProto.getHandshakeSchema();
			}
			ObjectMapper om = new ObjectMapper();
			sendDataToServer(om.writeValueAsString(hs));
			String message;
			do {
				message = socketBufferReader.readLine();
				if(message != null) {
					processIncommingMessage(message);	
				}
			} while(scanning && message != null);
			logger.debug("Finish thread by: " + (message == null ? "Null message received (connection lost?)" : "scanning off"));
		} catch(IOException e) {
			logger.error("MAIN THREAD IO EXCEPTION: ", e);
		}
	}
	
	private void createSocketConneciton() throws UnknownHostException, IOException {
		socket = new Socket(remoteAddr, this.remoteListenerPort);
		socketBufferReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        socketBufferOutput = new PrintWriter(socket.getOutputStream());
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
		if("eppr/room-auth".equals(schema.namespace)) {
			switch(schema.schema) {
			case "signup":
				processSignupSchema(schemaBody);
				break;
			case "signupResponse":
				processSignupResponseSchema(schemaBody);
				break;
			case "retry":
				processRetrySchema(schemaBody);
				break;
			case "rejected":
				processRejectedSchema(schemaBody);
				break;
			case "exceeded":
				processExceededSchema(schemaBody);
				break;
			case "tokenUpdate":
				processTokenUpdate(schemaBody);
				break;
			case "busy":
				processBusySchema(schemaBody);
				break;
			default:
				logger.debug("Schema not recognized: "+schema.schema);
				break;
			}
		}
	}
	
	private void processSignupSchema(String schemaBody) throws JsonProcessingException {
		logger.debug("Processing processSignupSchema.");
		SignupData signupData = roomAuthProto.getSignupSchema();
		ObjectMapper om = new ObjectMapper();
		sendDataToServer(om.writeValueAsString(signupData));
	}
	
	private void processSignupResponseSchema(String schemaBody) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			SignupResponse signupResponse = objectMapper.readValue(schemaBody, SignupResponse.class);
			Handshake handshake = roomAuthProto.getHandshakeSchema();
			handshake.serverID = signupResponse.serverID;
			handshake.securityToken = signupResponse.securityToken;
			objectMapper.writeValue(new File(cfgFileSave + File.separator + "lastHandshake.json"), handshake);
			logger.debug("Processed processSignupResponseSchema. New server ID:" + signupResponse.serverID);
		} catch(IOException e) {
			logger.error("I/O Exception in processSignupResponseSchema", e);
		}
	}
	
	// FIXME: validate retry times
	private void processRetrySchema(String schemaBody) throws JsonProcessingException {
		logger.debug("Processing processRetrySchema.");
		try {
			Handshake hs = roomAuthProto.getHandshakeSchema();
			ObjectMapper om = new ObjectMapper();
			sendDataToServer(om.writeValueAsString(hs));
		} catch (IOException e) {
			logger.error("I/O Exception (processRetrySchema): ", e);
		}
	}
	
	private void processRejectedSchema(String schemaBody) {
		logger.error("The registration was rejected by the Orchestrator server.");
		// TODO: check this, verify if really close the backend:
//		((ConfigurableApplicationContext) context).close();
	}
	
	private void processExceededSchema(String schemaBody) {
		logger.error("You exceeded the limit of signups.");
		// TODO: check this, verify if really close the backend:
//		((ConfigurableApplicationContext) context).close();
	}
	
	// TODO: finish this
	private void processTokenUpdate(String schemaBody) throws JsonParseException, JsonMappingException {
		try {
			ObjectMapper om = new ObjectMapper();
			TokenUpdate signupResponse = om.readValue(schemaBody, TokenUpdate.class);
			File configuration = new File(cfgFileSave + File.separator + "lastHandshake.json");
			if(configuration.exists()) {
				logger.debug("Processing processTokenUpdate. new token ["+signupResponse.securityToken+"]");
				ObjectMapper objectMapper = new ObjectMapper();
				Handshake handshake = objectMapper.readValue(configuration, Handshake.class);
				handshake.securityToken = signupResponse.securityToken;
				objectMapper.writeValue(new File(cfgFileSave + File.separator + "lastHandshake.json"), handshake);
			} else {
				logger.error("Configuration file isn't exists and cant be updated with new token: " + signupResponse.securityToken);
			}
		} catch (IOException e) {
			logger.error("I/O Exception in processTokenUpdate", e);
		}
	}
	
	private void processBusySchema(String schemaBody) throws JsonProcessingException {
		logger.debug("Processing processBusySchema.");
		try {
			Handshake hs = roomAuthProto.getHandshakeSchema();
			ObjectMapper om = new ObjectMapper();
			logger.info("Waiting for...");
			Thread.sleep(3500); // TODO: param this 3500 sleep time.
			logger.info("Retrying");
			sendDataToServer(om.writeValueAsString(hs));
		} catch (IOException e) {
			logger.error("I/O Exception in process busy schema: ", e);
		} catch (InterruptedException e) {
			logger.error("Interrupted Exception in process busy schema:", e);
		}
	}

}
