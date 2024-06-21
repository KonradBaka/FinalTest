package pl.kurs.finaltest.services;

import pl.kurs.finaltest.database.entity.ImportStatus;
import pl.kurs.finaltest.database.repositories.ImportSessionRepository;
import pl.kurs.finaltest.exceptions.SessionNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IImportSessionService {

    Long createImportSession();

    void updateImportSessionStatus(Long sessionId, String status);

    ImportStatus getImportStatus(Long sessionId);

    List<ImportStatus> getAllSessions();

    void incrementRecordsProcessed(Long sessionId, int incrementBy);
}
