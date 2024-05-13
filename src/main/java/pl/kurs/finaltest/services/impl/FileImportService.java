package pl.kurs.finaltest.services.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.finaltest.database.entity.ImportStatus;
import pl.kurs.finaltest.database.entity.Person;
import pl.kurs.finaltest.database.repositories.ImportSessionRepository;
import pl.kurs.finaltest.dto.PersonDto;
import pl.kurs.finaltest.exceptions.ImportInProgressException;
import pl.kurs.finaltest.exceptions.InvalidInputData;
import pl.kurs.finaltest.exceptions.SessionNotFoundException;
import pl.kurs.finaltest.services.IFileImportService;
import pl.kurs.finaltest.stategy.PersonTypeStrategy;
import pl.kurs.finaltest.stategy.PersonStrategyManager;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class FileImportService implements IFileImportService {

    private final CsvImportService csvImportService;
    private final ImportSessionService importSessionService;
    private final LockManagerService lockManager;

    public FileImportService(CsvImportService csvImportService, ImportSessionService importSessionService, LockManagerService lockManager) {
        this.csvImportService = csvImportService;
        this.importSessionService = importSessionService;
        this.lockManager = lockManager;
    }

    @Async("fileImportTaskExecutor")
    public CompletableFuture<Long> importFile(MultipartFile file) {
        return importSessionService.createImportSession()
                .thenCompose(sessionId -> {
                    if (!lockManager.acquireLock("import_process")) {
                        importSessionService.updateImportSessionStatus(sessionId, "FAILED");
                        return CompletableFuture.failedFuture(new ImportInProgressException("Inny proces w toku"));
                    }
                    try {
                        ImportStatus session = importSessionService.getImportStatus(sessionId);
                        try (InputStream inputStream = file.getInputStream()) {
                            csvImportService.parseCsv(inputStream, session);
                            importSessionService.updateImportSessionStatus(sessionId, "COMPLETED");
                            return CompletableFuture.completedFuture(sessionId);
                        } catch (Exception e) {
                            importSessionService.updateImportSessionStatus(sessionId, "FAILED");
                            throw new InvalidInputData("Nie udało się przetworzyć pliku", e);
                        }
                    } finally {
                        lockManager.releaseLock("import_process");
                    }
                });
    }
}





