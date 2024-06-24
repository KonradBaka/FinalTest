package pl.kurs.finaltest.services;

public interface IImportQueueService {

    void addImportTask(Runnable importTask);

}
