package com.tandilserver.poker_lobby.socketRooms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.tandilserver.poker_lobby.socketRooms.services.RoomService;

@Configuration
public class RoomRegisterConfiguration {

	@Bean(value="taskExecutor")
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setThreadNamePrefix("RoomRegisterService");
        executor.initialize();
        return executor;
    }
	
}
