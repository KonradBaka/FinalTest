package pl.kurs.finaltest.services.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.finaltest.database.entity.ImportStatus;
import pl.kurs.finaltest.exceptions.ImportInProgressException;
import pl.kurs.finaltest.exceptions.InvalidInputData;
import pl.kurs.finaltest.services.IFileImportService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileImportService implements IFileImportService {

    private CsvImportService csvImportService;
    private ImportSessionService importSessionService;
    private LockManagerService lockManager;

    public FileImportService(CsvImportService csvImportService, ImportSessionService importSessionService, LockManagerService lockManager) {
        this.csvImportService = csvImportService;
        this.importSessionService = importSessionService;
        this.lockManager = lockManager;
    }

    public Long initiateImportSession() {
        return importSessionService.createImportSession();
    }


    @Async("fileImportTaskExecutor")
    public void importFile(String filePath, Long sessionId) {
        System.out.println("Rozpoczęcie importu: " + sessionId);//do kontroli błędów
        try {
            if (!lockManager.acquireLock("import_process")) {
                importSessionService.updateImportSessionStatus(sessionId, "FAILED");
                throw new ImportInProgressException("Inny proces w toku");
            }
            try (InputStream inputStream = new FileInputStream(filePath)) {
                ImportStatus session = importSessionService.getImportStatus(sessionId);
                csvImportService.parseCsv(inputStream, session);
                importSessionService.updateImportSessionStatus(sessionId, "COMPLETED");
                System.out.println("Import zakończony sukcesem: " + sessionId);
            } catch (Exception e) {
                importSessionService.updateImportSessionStatus(sessionId, "FAILED");
                System.err.println("Błąd importu: " + sessionId);
                e.printStackTrace();
                throw new InvalidInputData("Nie udało się przetworzyć pliku", e);
            } finally {
                lockManager.releaseLock("import_process");
                try {
                    Files.deleteIfExists(Paths.get(filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            importSessionService.updateImportSessionStatus(sessionId, "FAILED");
            System.err.println("Error przy uploadzie pliku: " + sessionId);
            e.printStackTrace();
        }

    }
}







