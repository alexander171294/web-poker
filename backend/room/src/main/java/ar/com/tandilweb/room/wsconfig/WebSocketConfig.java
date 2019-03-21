package ar.com.tandilweb.room.wsconfig;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurationSupport;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.RequestUpgradeStrategy;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends  WebSocketMessageBrokerConfigurationSupport implements WebSocketMessageBrokerConfigurer {

	@Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/clientInterceptor"); // prefijos de salida
        config.setApplicationDestinationPrefixes("/stompApi"); // prefijos de entrada (api)
        config.setUserDestinationPrefix("/userInterceptor"); // prefijo para single user (tome)
    }
	
	public void registerStompEndpoints(StompEndpointRegistry registry) {
        RequestUpgradeStrategy upgradeStrategy = new TomcatRequestUpgradeStrategy();

        registry.addEndpoint("/external").setAllowedOrigins("http://localhost:8000", "*").addInterceptors(new IpHandshakeInterceptor()).withSockJS(); //endpoints externos
        registry.addEndpoint("/external")
        .setHandshakeHandler(new DefaultHandshakeHandler(upgradeStrategy))
        .setAllowedOrigins("*")
        .addInterceptors(new IpHandshakeInterceptor());
    }
	
	@Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
	        return super.configureMessageConverters(messageConverters);
	}

	@Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        super.addReturnValueHandlers(returnValueHandlers);
    }
    
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        super.configureClientInboundChannel(registration);
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        super.configureClientOutboundChannel(registration);
	}
    
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        super.configureWebSocketTransport(registry);
	}
    
    @Bean
    public WebSocketHandler subProtocolWebSocketHandler() {
	        return new WebSocketMonitor(clientInboundChannel(), clientOutboundChannel());
	}
	
}
