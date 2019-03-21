package ar.com.tandilweb.room.clientControllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@MessageMapping("/demoController")
public class DemoController {

	// Controller for chat.
	private static final Logger log = LoggerFactory.getLogger(DemoController.class);

	// mapping from /stompApi/chatService/ping
	@MessageMapping("/ping")
	public String ping() {
		log.info("Ping in");
		return "PONG";
	}

}
