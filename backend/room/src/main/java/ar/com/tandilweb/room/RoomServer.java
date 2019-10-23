package ar.com.tandilweb.room;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import ar.com.tandilweb.room_int.GameCtrlInt;
import ar.com.tandilweb.room_poker.PokerRoom;

@SpringBootApplication
@Configuration
@PropertySources(value = {
		@PropertySource("classpath:clientRegisterConfig.properties"),
		@PropertySource("classpath:clientDetails.properties")
	})
public class RoomServer  extends SpringBootServletInitializer
{
    public static void main( String[] args )
    {
    	SpringApplication.run(RoomServer.class, args);
    }
    
    @Bean()
    public GameCtrlInt gameCtrl() {
    	// TODO: check configuration for construct the specific class for all games.
    	// TODO: send the server configuration to a constructor.
    	return new PokerRoom();
    }
}
