package ar.com.tandilweb.room.orchestratorBridge;

import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.com.tandilweb.exchange.ServerRecordingSchema;
import ar.com.tandilweb.room_int.OrchestratorPipe;

public class OrchestratorPipeImpl implements OrchestratorPipe {
	
	public static Logger logger = LoggerFactory.getLogger(OrchestratorPipeImpl.class);
	
	private PrintWriter socketBufferOutput;
	
	public OrchestratorPipeImpl(PrintWriter socketBufferOutput) {
		this.socketBufferOutput = socketBufferOutput;
	}
	
	public boolean SendToOrchestrator(ServerRecordingSchema srs) {
		ObjectMapper om = new ObjectMapper();
		try {
			socketBufferOutput.println(om.writeValueAsString(srs));
			socketBufferOutput.flush();
			return true;
		} catch (JsonProcessingException e) {
			logger.error("Error writting in Orchestrator pipe", e);
		}
		return false;
	}

}
