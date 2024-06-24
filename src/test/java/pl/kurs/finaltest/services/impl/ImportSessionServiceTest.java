package pl.kurs.finaltest.services.impl;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import pl.kurs.finaltest.database.entity.ImportStatus;
import pl.kurs.finaltest.database.repositories.ImportSessionRepository;
import pl.kurs.finaltest.exceptions.SessionNotFoundException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ImportSessionServiceTest {

    @Autowired
    private ImportSessionRepository importSessionRepository;

    @Autowired
    private ImportSessionService importSessionService;

    @BeforeEach
    void setUp() {
        importSessionRepository.deleteAll();
    }


    @Test
    @Transactional
    void createImportSessionShouldCreateNewSession() {
        // When
        Long sessionId = importSessionService.createImportSession();

        // Then
        assertNotNull(sessionId, "ID sesji nie powinno być nullem");
        ImportStatus session = importSessionRepository.findById(sessionId).orElse(null);
        assertNotNull(session, "Sesja powinna zostać zapisana w bazie danych");
        assertEquals("IN_PROGRESS", session.getStatus(), "Status sesji powinien być 'IN_PROGRESS'");
    }

    @Test
    @Transactional
    void updateImportSessionStatusShouldUpdateStatus() {
        // Given
        ImportStatus session = new ImportStatus();
        session.setStartTime(LocalDateTime.now());
        session.setStatus("IN_PROGRESS");
        session = importSessionRepository.save(session);

        // When
        importSessionService.updateImportSessionStatus(session.getId(), "COMPLETED");

        // Then
        ImportStatus updatedSession = importSessionRepository.findById(session.getId()).orElseThrow();
        assertEquals("COMPLETED", updatedSession.getStatus());
        assertNotNull(updatedSession.getEndTime());
    }

    @Test
    @Transactional
    void getImportStatusShouldReturnSession() {
        // Given
        ImportStatus session = new ImportStatus();
        session.setStartTime(LocalDateTime.now());
        session.setStatus("IN_PROGRESS");
        session = importSessionRepository.save(session);

        // When
        ImportStatus result = importSessionService.getImportStatus(session.getId());

        // Then
        assertNotNull(result);
        assertEquals(session.getId(), result.getId());
    }

    @Test
    @Transactional
    void getImportStatusShouldThrowExceptionIfNotFound() {
        // Given
        Long invalidId = 999L;

        // When & Then
        assertThrows(SessionNotFoundException.class, () -> importSessionService.getImportStatus(invalidId));
    }

    @Test
    @Transactional
    void incrementRecordsProcessedShouldIncrementRecordCount() {
        // Given
        ImportStatus session = new ImportStatus();
        session.setStartTime(LocalDateTime.now());
        session.setStatus("IN_PROGRESS");
        session.setRecordsProcessed(5);
        session = importSessionRepository.save(session);

        // When
        importSessionService.incrementRecordsProcessed(session.getId(), 1);

        // Then
        ImportStatus updatedSession = importSessionRepository.findById(session.getId()).orElseThrow();
        assertEquals(6, updatedSession.getRecordsProcessed());
    }

    @Test
    @Transactional
    void incrementRecordsProcessedShouldIncrementBySpecifiedAmount() {
        // Given
        ImportStatus session = new ImportStatus();
        session.setStartTime(LocalDateTime.now());
        session.setStatus("IN_PROGRESS");
        session.setRecordsProcessed(5);
        session = importSessionRepository.save(session);

        // When
        importSessionService.incrementRecordsProcessed(session.getId(), 3);

        // Then
        ImportStatus updatedSession = importSessionRepository.findById(session.getId()).orElseThrow();
        assertEquals(8, updatedSession.getRecordsProcessed());
    }

}
