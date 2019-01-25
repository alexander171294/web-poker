package com.tandilserver.poker_room.registerService;

import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class UpdaterInfoServiceThread implements Runnable {
	
	public static Logger logger = LoggerFactory.getLogger(RegisterServiceThread.class);
	
	@Autowired
	private ServerDataBlock srvDataBlock;
	
	private PrintWriter socketBufferOutput;

	@Override
	public void run() {
		logger.debug("Sending updated information.");
		ObjectMapper oM = new ObjectMapper();
		try {
			socketBufferOutput.println(oM.writeValueAsString(srvDataBlock.getSrvInfo()));
			socketBufferOutput.flush();
		} catch (JsonProcessingException e) {
			logger.error("Cant send server info updated", e);
		}
	}

	public PrintWriter getSocketBufferOutput() {
		return socketBufferOutput;
	}

	public void setSocketBufferOutput(PrintWriter socketBufferOutput) {
		this.socketBufferOutput = socketBufferOutput;
	}
	
	
}
