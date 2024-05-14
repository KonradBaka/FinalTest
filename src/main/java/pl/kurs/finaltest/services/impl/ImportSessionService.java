package pl.kurs.finaltest.services.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.kurs.finaltest.database.entity.ImportStatus;
import pl.kurs.finaltest.database.repositories.ImportSessionRepository;
import pl.kurs.finaltest.exceptions.SessionNotFoundException;
import pl.kurs.finaltest.services.IImportSessionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ImportSessionService implements IImportSessionService {

    private ImportSessionRepository importSessionRepository;

    public ImportSessionService(ImportSessionRepository importSessionRepository) {
        this.importSessionRepository = importSessionRepository;
    }

    @Override
    public Long createImportSession() {
        ImportStatus session = new ImportStatus();
        session.setStartTime(LocalDateTime.now());
        session.setStatus("IN_PROGRESS");
        importSessionRepository.save(session);
        return session.getId();
    }

    @Override
    public void updateImportSessionStatus(Long sessionId, String status) {
        ImportStatus session = importSessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Nie znaleziono sesji: " + sessionId));
        session.setStatus(status);
        session.setEndTime(LocalDateTime.now());
        importSessionRepository.save(session);
    }

    @Override
    public ImportStatus getImportStatus(Long sessionId) {
        return importSessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Nie znaleziono sesji: " + sessionId));
    }

    @Override
    public List<ImportStatus> getAllSessions() {
        return importSessionRepository.findAll();
    }

    @Override
    public void incrementRecordsProcessed(Long sessionId) {
        ImportStatus session = importSessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Nie znaleziono sesji: " + sessionId));
        session.setRecordsProcessed(session.getRecordsProcessed() + 1);
        importSessionRepository.save(session);
    }
}
