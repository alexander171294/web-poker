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
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

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
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// TODO Auto-generated method stub
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
	
	private void processIncommingMessage(String message) {
		// TODO: pending...
	}

}
