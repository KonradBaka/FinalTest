package pl.kurs.finaltest.services.impl;

import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.finaltest.database.entity.ImportStatus;
import pl.kurs.finaltest.exceptions.ImportInProgressException;
import pl.kurs.finaltest.exceptions.InvalidInputData;
import pl.kurs.finaltest.services.IFileImportService;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

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
    public void importFile(MultipartFile file, Long sessionId) {
        if (!lockManager.acquireLock("import_process")) {
            importSessionService.updateImportSessionStatus(sessionId, "FAILED");
            throw new ImportInProgressException("Inny proces w toku");
        }
        try {
            ImportStatus session = importSessionService.getImportStatus(sessionId);
            processFile(file, session);
            importSessionService.updateImportSessionStatus(sessionId, "COMPLETED");
        } catch (Exception e) {
            importSessionService.updateImportSessionStatus(sessionId, "FAILED");
            throw new InvalidInputData("Nie udało się przetworzyć pliku");
        } finally {
            lockManager.releaseLock("import_process");
        }
    }

    @Transactional
    protected void processFile(MultipartFile file, ImportStatus session) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            csvImportService.parseCsv(inputStream, session);
        }
    }
}





