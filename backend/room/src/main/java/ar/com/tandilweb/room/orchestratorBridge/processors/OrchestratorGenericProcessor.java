package ar.com.tandilweb.room.orchestratorBridge.processors;

import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Value;

public class OrchestratorGenericProcessor {
	
	@Value("${act.room.cfgFileSave}")
	protected String cfgFileSave;
	
	private PrintWriter socketBufferOutput;
	
	public PrintWriter getSocketBufferOutput() {
		return socketBufferOutput;
	}

	public void setSocketBufferOutput(PrintWriter socketBufferOutput) {
		this.socketBufferOutput = socketBufferOutput;
	}
	
	public void sendDataToServer(String data) {
		socketBufferOutput.println(data);
		socketBufferOutput.flush();
	}

}
