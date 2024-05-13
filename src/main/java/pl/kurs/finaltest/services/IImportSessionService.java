package pl.kurs.finaltest.services;

import pl.kurs.finaltest.database.entity.ImportStatus;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IImportSessionService {

    CompletableFuture<Long> createImportSession();

    void updateImportSessionStatus(Long sessionId, String status);

    ImportStatus getImportStatus(Long sessionId);

    List<ImportStatus> getAllSessions();

    void incrementRecordsProcessed(Long sessionId);

}
