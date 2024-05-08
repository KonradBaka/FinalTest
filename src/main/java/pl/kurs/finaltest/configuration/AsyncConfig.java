package pl.kurs.finaltest.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync(proxyTargetClass=true)
public class AsyncConfig {


        @Bean(name ="fileImportTaskExecutor")
    public Executor fileImportTaskExecutor() {

        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(2);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(100);
        taskExecutor.setThreadNamePrefix("ImportThread-");
        taskExecutor.initialize();

        return taskExecutor;
    }
}
