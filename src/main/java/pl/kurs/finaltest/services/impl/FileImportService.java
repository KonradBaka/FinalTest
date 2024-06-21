package pl.kurs.finaltest.services.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
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


    @Async("fileImport")
    public void importFile(String filePath, Long sessionId) {
        try {
            if (!lockManager.acquireLock("import_process")) {
                importSessionService.updateImportSessionStatus(sessionId, "FAILED");
                throw new ImportInProgressException("Inny proces w toku");
            }
            try (InputStream inputStream = new FileInputStream(filePath)) {
                ImportStatus session = importSessionService.getImportStatus(sessionId);
                csvImportService.parseCsv(inputStream, session);
                importSessionService.updateImportSessionStatus(sessionId, "COMPLETED");
            } catch (Exception e) {
                importSessionService.updateImportSessionStatus(sessionId, "FAILED");
                throw new InvalidInputData("Nie udało się przetworzyć pliku", e);
            } finally {
                lockManager.releaseLock("import_process");
                try {
                    Files.deleteIfExists(Paths.get(filePath));
                } catch (IOException e) {
                    throw new InvalidInputData("Nie udało się przetworzyć pliku", e);
                }
            }
        } catch (Exception e) {
            importSessionService.updateImportSessionStatus(sessionId, "FAILED");
        }
    }
}







