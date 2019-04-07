package ar.com.tandilweb.room;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@Configuration
@PropertySources(value = {
		@PropertySource("classpath:clientRegisterConfig.properties")
	})
public class App  extends SpringBootServletInitializer
{
    public static void main( String[] args )
    {
    	SpringApplication.run(App.class, args);
    }
}
