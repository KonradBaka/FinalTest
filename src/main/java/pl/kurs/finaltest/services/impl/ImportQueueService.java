package pl.kurs.finaltest.services.impl;

import org.springframework.stereotype.Service;
import pl.kurs.finaltest.services.IImportQueueService;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

@Service
public class ImportQueueService implements IImportQueueService {

    private final BlockingQueue<Runnable> importQueue;
    private final ExecutorService importExecutorService;

    public ImportQueueService(BlockingQueue<Runnable> importQueue, ExecutorService importExecutorService) {
        this.importQueue = importQueue;
        this.importExecutorService = importExecutorService;

        startProcessingQueue();
    }

    private void startProcessingQueue() {
        importExecutorService.submit(() -> {
            while (true) {
                try {
                    Runnable importTask = importQueue.take();
                    importTask.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    @Override
    public void addImportTask(Runnable importTask) {
        importQueue.offer(importTask);
    }
}
