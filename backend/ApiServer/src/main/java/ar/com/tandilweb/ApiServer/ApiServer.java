package ar.com.tandilweb.ApiServer;

import javax.servlet.Filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import ar.com.tandilweb.ApiServer.filters.CorsFilter;
import ar.com.tandilweb.ApiServer.filters.jwt.JWTFilter;
import ar.com.tandilweb.ApiServer.filters.jwt.JWTValidFilter;
import ar.com.tandilweb.ApiServer.persistence.JDBConfiguration;

@SpringBootApplication
@EntityScan("ar.com.tandilweb.ApiServer.dataBase.domain")
@ComponentScan({"ar.com.tandilweb.ApiServer.rests", "ar.com.tandilweb.ApiServer.transport"})
@PropertySources(value = {
		@PropertySource("classpath:database.properties"),
		@PropertySource("classpath:apiServer.properties"),
	})
@Configuration("ar.com.tandilweb.ApiServer.persistence.JDBConfig")
@Import({JDBConfiguration.class})
public class ApiServer extends SpringBootServletInitializer {
	
	public static void main(String[] args) {
		SpringApplication.run(ApiServer.class, args);
	}

	@Bean
	public FilterRegistrationBean<Filter> registerCorsFilter() {
		FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<Filter>();
		registration.setFilter(corsFilter());
		registration.addUrlPatterns("/*");
		// registration.addInitParameter("paramName", "paramValue");
		registration.setName("corsFilter");
		registration.setOrder(1);
		return registration;
	}

	public Filter corsFilter() {
		return new CorsFilter();
	}

	@Bean
	public FilterRegistrationBean<Filter> registerJwtFilter() {
		FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<Filter>();
		registration.setFilter(jwtFilter());
		addPatternsJWT(registration);
		registration.setName("jwtFilter");
		registration.setOrder(2);
		return registration;
	}

	public Filter jwtFilter() {
		return new JWTFilter();
	}

	@Bean
	public FilterRegistrationBean<Filter> registerJwtValidFilter() {
		FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<Filter>();
		registration.setFilter(jwtValidFilter());
		addPatternsJWT(registration);
		registration.setName("jwtFilter");
		registration.setOrder(3);
		return registration;
	}

	public Filter jwtValidFilter() {
		return new JWTValidFilter();
	}

	public static void addPatternsJWT(FilterRegistrationBean<Filter> registration) {
		registration.addUrlPatterns("/administration/*", "/friends/*", "/lobby/*", "/users/*");
	}

}
