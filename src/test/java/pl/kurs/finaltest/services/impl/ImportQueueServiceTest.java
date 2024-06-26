package pl.kurs.finaltest.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ContextConfiguration(classes = ImportQueueServiceTest.TestConfig.class)
public class ImportQueueServiceTest {

    @Configuration
    static class TestConfig {

        @Bean
        public BlockingQueue<Runnable> importQueue() {
            importQueue = new ArrayBlockingQueue<>(10);
            return importQueue;
        }

        @Bean
        public ExecutorService importExecutorService() {
            importExecutorService = Executors.newSingleThreadExecutor();
            return importExecutorService;
        }

        @Bean
        public ImportQueueService importQueueService() {
            return new ImportQueueService(importQueue, importExecutorService);
        }
    }


    @Autowired
    private ImportQueueService importQueueService;

    private static BlockingQueue<Runnable> importQueue;
    private static ExecutorService importExecutorService;

    @BeforeEach
    void setUp() {
        importQueue.clear();
    }

    @Test
    void testAddImportTask() throws InterruptedException {
        // Given
        Runnable task = () -> System.out.println("Task executed");

        // When
        importQueueService.addImportTask(task);

        // Then
        assertEquals(1, importQueue.size(), "Import queue should contain 1 task");
    }

    @Test
    void testImportTaskExecution() throws InterruptedException {
        // Given
        final boolean[] taskExecuted = {false};
        Runnable task = () -> taskExecuted[0] = true;

        // When
        importQueueService.addImportTask(task);

        // Then
        TimeUnit.SECONDS.sleep(1); // Wait for the task to be executed
        assertTrue(taskExecuted[0], "Task should be executed");
    }
}