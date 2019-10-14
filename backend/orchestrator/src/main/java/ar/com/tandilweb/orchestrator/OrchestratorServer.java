package ar.com.tandilweb.orchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import ar.com.tandilweb.orchestrator.persistence.JDBConfig;

@SpringBootApplication
@PropertySources(value = {
			@PropertySource("classpath:serverRegisterConfig.properties"),
			@PropertySource("classpath:database.properties"),
		})
@Import({ JDBConfig.class })
public class OrchestratorServer extends SpringBootServletInitializer
{
    public static void main( String[] args )
    {
    	SpringApplication.run(OrchestratorServer.class, args);
    }
}
