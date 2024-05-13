package pl.kurs.finaltest.services.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class FileImportService implements IFileImportService {

    private PersonStrategyManager strategyManager;
    private ImportSessionRepository importSessionRepository;
    private LockManagerService lockManager;


    public FileImportService(PersonStrategyManager strategyManager, ImportSessionRepository importSessionRepository, LockManagerService lockManager) {
        this.strategyManager = strategyManager;
        this.importSessionRepository = importSessionRepository;
        this.lockManager = lockManager;
    }


    @Async("fileImportTaskExecutor")
    public CompletableFuture<Long> importFile(Long sessionId, MultipartFile file) throws InterruptedException {
        if (!lockManager.acquireLock("import_process")) { //sprawdzanie czy nie trwa inny import, wykonywane na bazie danych wiec dziła webowo
            updateSessionStatus(sessionId, "FAILED");
            throw new ImportInProgressException("Obecnie trwa inny proces.");
        }
        try {
            ImportStatus session = importSessionRepository.findById(sessionId)
                    .orElseThrow(() -> new SessionNotFoundException("Nie znaleziono sesji: " + sessionId));

            session.setStatus("IN_PROGRESS");
            importSessionRepository.save(session);

            CompletableFuture<Void> task = CompletableFuture.runAsync(() -> { //nieblokujące przetwarzanie rekordów, które mogą trwać długo w celu nie blokowania http
                try (InputStream inputStream = file.getInputStream()) {
                    parseCsv(inputStream, session);
                } catch (Exception e) {
                    throw new InvalidInputData("Nie udało się przetworzyć pliku", e);
                }
            });

            task.thenRunAsync(() -> updateSessionStatus(sessionId, "COMPLETED"))
                    .exceptionally(e -> {
                        updateSessionStatus(sessionId, "FAILED");
                        return null;
                    });

            return CompletableFuture.completedFuture(sessionId);
        } finally {
            lockManager.releaseLock("import_process");
        }
    }

    @Transactional
    protected void parseCsv(InputStream inputStream, ImportStatus session) throws IOException {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<Map<String, String>> records = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                records.add(record.toMap());
            }
            records.forEach(this::processRecord);
            updateSessionProgress(session, csvParser.getRecordNumber());
        }
    }

    private void processRecord(Map<String, String> record) {
        String type = record.get("type");
        PersonTypeStrategy<? extends Person, ? extends PersonDto> strategy = strategyManager.getStrategy(type);
        strategy.importFromCsvRecord(record);
    }

    public void updateSessionProgress(ImportStatus session, long count) {
        session.incrementRecordsProcessed(count);
        importSessionRepository.save(session);
    }

    public void updateSessionStatus(Long sessionId, String status) {
        ImportStatus session = importSessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Nie znaleziono sesji: " + sessionId));
        session.setStatus(status);
        session.setEndTime(LocalDateTime.now());
        importSessionRepository.save(session);
    }

    public Long createNewImportSession() {
        ImportStatus session = new ImportStatus();
        session.setStartTime(LocalDateTime.now());
        session.setStatus("IN_PROGRESS");
        importSessionRepository.save(session);
        return session.getId();
    }
}





