package com.tandilserver.poker_room;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class App  extends SpringBootServletInitializer
{
	
	public static void main( String[] args )
    {
    	SpringApplication.run(App.class, args);
    }
    
}
