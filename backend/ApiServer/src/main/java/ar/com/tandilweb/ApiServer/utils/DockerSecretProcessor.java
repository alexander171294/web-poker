package ar.com.tandilweb.ApiServer.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.util.FileCopyUtils;

// based on: https://github.com/kwonghung-YIP/spring-boot-docker-secret

public class DockerSecretProcessor implements EnvironmentPostProcessor {
	
	public static Logger logger = LoggerFactory.getLogger(DockerSecretProcessor.class);

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		String bindPathPpty = environment.getProperty("docker-secret.bind-path");
		bindPathPpty = bindPathPpty == null ? "/run/secrets" : bindPathPpty;
		logger.info("Processing docker secrets from: " + bindPathPpty);
		Path bindPath = Paths.get(bindPathPpty);
		if (Files.isDirectory(bindPath)) {
			Map<String,Object> dockerSecrets;
			try {
				dockerSecrets = 
				  Files.list(bindPath)
				    .collect(
				      Collectors.toMap(
				        path -> {
				        	File secretFile = path.toFile();
				        	return "docker-secret-" + secretFile.getName();
				        },
				        path -> {
				        	File secretFile = path.toFile();
							try {
								byte[] content = FileCopyUtils.copyToByteArray(secretFile);
								return new String(content);
							} catch (IOException e) {
								throw new RuntimeException(e);
							}
				        }
				      ));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
			dockerSecrets
			  .entrySet()
			    .forEach(entry -> {
			    	logger.debug(entry.getKey()+"=\""+entry.getValue()+"\"");
			    });
			
			MapPropertySource pptySource = new MapPropertySource("docker-secrets",dockerSecrets);
			
			environment.getPropertySources().addLast(pptySource);
			
		}
	}

}
