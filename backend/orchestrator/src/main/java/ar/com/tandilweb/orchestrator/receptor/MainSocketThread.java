package ar.com.tandilweb.orchestrator.receptor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import ar.com.tandilweb.orchestrator.handlers.RoomHandlerThread;

@Component
@PropertySources(value = { @PropertySource("classpath:roomRegisterService.properties") })
public class MainSocketThread implements Runnable, ApplicationListener<ContextRefreshedEvent>, DisposableBean {

	public static Logger logger = LoggerFactory.getLogger(MainSocketThread.class);

	private Thread thread;
	private volatile boolean stopRequest;

	@Value("${room.register.listenPort}")
	private volatile int listenPort;

	@Autowired
	private TaskExecutor taskExecutor;

	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public void run() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			logger.debug("Starting Register Server in port " + listenPort);
			serverSocket = new ServerSocket(listenPort);
		} catch (IOException e) {
			logger.error("Error creating RoomRegisterService", e);
		}
		while (!stopRequest) {
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				logger.error("Socket I/O Exception", e);
			}
			RoomHandlerThread roomHandlerThread = applicationContext.getBean(RoomHandlerThread.class);
			roomHandlerThread.setName(UUID.randomUUID().toString());
			roomHandlerThread.setSocket(socket);
			taskExecutor.execute(roomHandlerThread);
			// taskExecutor.execute(new (socket, UUID.randomUUID().toString()));
		}
	}
	
	@Override
	public void destroy() throws Exception {
		stopRequest = true;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		if(this.thread == null) {
			this.thread = new Thread(this);
			this.thread.start();
			logger.debug("Main thread created.");
		}
	}

}
