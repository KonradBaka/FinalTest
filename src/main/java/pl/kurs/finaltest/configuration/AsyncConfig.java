package pl.kurs.finaltest.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync(proxyTargetClass = true)
public class AsyncConfig {

    @Bean(name = "fileImport")
    public Executor fileImportTaskExecutor() {

        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(20);
        taskExecutor.setQueueCapacity(500);
        taskExecutor.setThreadNamePrefix("ImportThread-");
        taskExecutor.initialize();
        return taskExecutor;
    }

}
