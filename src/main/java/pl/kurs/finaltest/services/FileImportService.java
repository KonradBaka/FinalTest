package pl.kurs.finaltest.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.finaltest.dto.PersonDto;
import pl.kurs.finaltest.dto.TypeIdentifierDto;
import pl.kurs.finaltest.exceptions.ImportInProgressException;
import pl.kurs.finaltest.models.ImportStatus;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.repositories.ImportSessionRepository;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class FileImportService implements IFileImportService {


    private CsvParserService csvParserService;
    private PersonStrategyManager strategyManager;
    private ImportSessionRepository importSessionRepository;
    private DistributedLockManager lockManager; // stworzyłem nowy serwis który implementuje RedisTemplate. Czytałem że to jedna z lepszy metod obsługi locków

    public FileImportService(CsvParserService csvParserService, PersonStrategyManager strategyManager, ImportSessionRepository importSessionRepository, DistributedLockManager lockManager) {
        this.csvParserService = csvParserService;
        this.strategyManager = strategyManager;
        this.importSessionRepository = importSessionRepository;
        this.lockManager = lockManager;
    }

    public Long importFile(MultipartFile file) throws ImportInProgressException {
        if (!lockManager.obtainLock()) { //Jezeli lock jest pusty to importFile jest lockowany
            throw new ImportInProgressException("Trwa kolejny import.");
        }

        ImportStatus session = createNewImportSession();
        processFile(file, session);
        return session.getId();
    }

    @Async
    public void processFile(MultipartFile file, ImportStatus session) {
        try (InputStream inputStream = file.getInputStream()) {
            processCsvRecords(inputStream, session);
            updateSessionStatus(session.getId(), "COMPLETED");
        } catch (Exception e) {
            updateSessionStatus(session.getId(), "FAILED");
            throw new RuntimeException("Nie udało się przetworzyć pliku", e);
        } finally {
            lockManager.releaseLock(); // Asynchronicznie wykonuje sie przetwarzanie pliku i lock zostaje zwolniony po jej wykonaniu lub błędzie
        }
    }

    @Transactional
    protected void importRecord(Map<String, String> record, ImportStatus session) {
        String type = record.get("TYPE");
        if (type != null) {
            PersonTypeStrategy<? extends Person, ? extends PersonDto> strategy = strategyManager.getStrategy(new TypeIdentifierDto(type));
            if (strategy != null) {
                strategy.importFromCsvRecord(record);
                synchronized (session) {
                    session.incrementRecordsProcessed();
                }
            } else {
                throw new RuntimeException("Nie znaleziono strategii dla typu: " + type);
            }
        } else {
            throw new RuntimeException("Brak typu rekordu");
        }
    }


    @Transactional
    public void updateSessionStatus(Long sessionId, String status) {
        ImportStatus session = importSessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono sesji: " + sessionId));
        session.setStatus(status);
        session.setEndTime(LocalDateTime.now());
        importSessionRepository.save(session);
    }


    @Transactional
    protected ImportStatus createNewImportSession() {
        ImportStatus session = new ImportStatus();
        session.setStartTime(LocalDateTime.now());
        session.setStatus("IN_PROGRESS");
        return importSessionRepository.save(session);
    }

    private void processCsvRecords(InputStream inputStream, ImportStatus session) {
        csvParserService.parseCsv(inputStream, record -> {
            try {
                importRecord(record, session);
            } catch (Exception e) {
            }
        });
    }
}


