package ar.com.tandilweb.room.orchestratorBridge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
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
	
	@Value("${ar.com.tandilweb.room.orchestratorBridge.OrchestratorThread.remoteListenerPort}")
    private volatile int remoteListenerPort;
	
	@Value("${ar.com.tandilweb.room.orchestratorBridge.OrchestratorThread.remoteAddr}")
	private volatile String remoteAddr;
	
//	@Autowired
//	private TaskScheduler taskScheduler;
	
	BufferedReader socketBufferReader;
	private PrintWriter socketBufferOutput;
	
//	@Autowired
//	private ApplicationContext context;
	
	private Socket socket;
	private boolean scanning;
	
	@Autowired
	private EpprRoomAuth roomAuthProto;
	
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
	
	private void main() throws IOException {
		scanning = true;
		Handshake hs = roomAuthProto.getHandshakeSchema();
		ObjectMapper om = new ObjectMapper();
		sendDataToServer(om.writeValueAsString(hs));
		do {
			String message = socketBufferReader.readLine();
			processIncommingMessage(message);
		} while(scanning);
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
	
	private String getMyLocalIP() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException ukHostException) {
			logger.error("Unknown Host Exception", ukHostException);
			return null;
		}
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
	
	// TODO: finish this and persist ID for next handshake
	private void processSignupResponseSchema(String schemaBody) throws JsonParseException, JsonMappingException, IOException {
		logger.debug("Processing processSignupResponseSchema.");
		ObjectMapper om = new ObjectMapper();
		SignupResponse signupResponse = om.readValue(schemaBody, SignupResponse.class);
		// signupResponse.serverID;
	}
	
	// FIXME: validate retry times
	private void processRetrySchema(String schemaBody) throws JsonProcessingException {
		logger.debug("Processing processRetrySchema.");
		Handshake hs = roomAuthProto.getHandshakeSchema();
		ObjectMapper om = new ObjectMapper();
		sendDataToServer(om.writeValueAsString(hs));
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
	
	// TODO: finish this and persist token for next handshake
	private void processTokenUpdate(String schemaBody) throws JsonParseException, JsonMappingException, IOException {
		logger.debug("Processing processTokenUpdate.");
		ObjectMapper om = new ObjectMapper();
		TokenUpdate signupResponse = om.readValue(schemaBody, TokenUpdate.class);
		// signupResponse.securityToken;
	}
	
	// FIXME: add sleep time.
	private void processBusySchema(String schemaBody) throws JsonProcessingException {
		logger.debug("Processing processBusySchema.");
		Handshake hs = roomAuthProto.getHandshakeSchema();
		ObjectMapper om = new ObjectMapper();
		sendDataToServer(om.writeValueAsString(hs));
	}

}
