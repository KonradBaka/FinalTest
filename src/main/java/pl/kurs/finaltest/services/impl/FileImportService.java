package pl.kurs.finaltest.services.impl;

import jakarta.transaction.Transactional;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.finaltest.database.entity.ImportStatus;
import pl.kurs.finaltest.database.entity.Person;
import pl.kurs.finaltest.database.repositories.ImportSessionRepository;
import pl.kurs.finaltest.database.repositories.PersonRepository;
import pl.kurs.finaltest.dto.PersonDto;
import pl.kurs.finaltest.exceptions.FailedImportException;
import pl.kurs.finaltest.exceptions.ImportInProgressException;
import pl.kurs.finaltest.exceptions.InvalidInputData;
import pl.kurs.finaltest.exceptions.SessionNotFoundException;
import pl.kurs.finaltest.services.IFileImportService;
import pl.kurs.finaltest.services.PersonTypeStrategy;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
public class FileImportService implements IFileImportService {

    private PersonStrategyManager strategyManager;
    private PersonRepository personRepository;
    private ImportSessionRepository importSessionRepository;
    private LockManagerService lockManager;
    private Executor executor;


    public FileImportService(PersonStrategyManager strategyManager, PersonRepository personRepository, ImportSessionRepository importSessionRepository, LockManagerService lockManager, Executor executor) {
        this.strategyManager = strategyManager;
        this.personRepository = personRepository;
        this.importSessionRepository = importSessionRepository;
        this.lockManager = lockManager;
        this.executor = executor;
    }

//    public CompletableFuture<Long> importFile(Long sessionId, MultipartFile file) {
//        if (!lockManager.acquireLock("import_process")) {
//            CompletableFuture<Long> failedFuture = new CompletableFuture<>();
//            failedFuture.completeExceptionally(new ImportInProgressException("Obecnie trwa inny proces."));
//            return failedFuture;
//        }
//
//        try {
//            ImportStatus session = importSessionRepository.findById(sessionId)
//                    .orElseThrow(() -> new SessionNotFoundException("Nie znaleziono sesji: " + sessionId));
//
//            return CompletableFuture.supplyAsync(() -> {
//                try {
//                    parseCsv(file.getInputStream(), session);
//                } catch (Exception e) {
//                    updateSessionStatus(sessionId, "FAILED");
//                    throw new RuntimeException("Nie udało się przetworzyć pliku", e);
//                } finally {
//                    lockManager.releaseLock("import_process");
//                }
//                return sessionId;
//            }, executor);
//        } catch (Exception e) {
//            lockManager.releaseLock("import_process");
//            throw e;
//        }
//    }
//
//    @Transactional
//    protected void parseCsv(InputStream inputStream, ImportStatus session) {
//        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
//             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
//
//            List<Person> persons = new ArrayList<>();
//            for (CSVRecord record : csvParser) {
//                Map<String, String> recordMap = record.toMap();
//                PersonTypeStrategy<? extends Person, ? extends PersonDto> strategy = strategyManager.getStrategy(record.get("type"));
//                Person person = strategy.importFromCsvRecord(recordMap);
//                persons.add(person);
//            }
//            personRepository.saveAll(persons);
//            updateSessionProgress(session, csvParser.getRecordNumber());
//            updateSessionStatus(session.getId(), "COMPLETED");
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to parse CSV records: " + e.getMessage(), e);
//        }
//    }
//
//    private void updateSessionProgress(ImportStatus session, long count) {
//        session.incrementRecordsProcessed(count);
//        importSessionRepository.save(session);
//    }
//
//    private void updateSessionStatus(Long sessionId, String status) {
//        ImportStatus session = importSessionRepository.findById(sessionId)
//                .orElseThrow(() -> new SessionNotFoundException("Nie znaleziono sesji: " + sessionId));
//        session.setStatus(status);
//        session.setEndTime(LocalDateTime.now());
//        importSessionRepository.save(session);
//    }

    @Async
    public CompletableFuture<Long> importFile(Long sessionId, MultipartFile file) throws InterruptedException {
        if (!lockManager.acquireLock("import_process")) {
            CompletableFuture<Long> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(new ImportInProgressException("Obecnie trwa inny proces."));
            return failedFuture;
        }
        try {
            ImportStatus session = importSessionRepository.findById(sessionId)
                    .orElseThrow(() -> new SessionNotFoundException("Nie znaleziono sesji: " + sessionId));
            try (InputStream inputStream = file.getInputStream()) {
                parseCsv(inputStream, session);
                updateSessionStatus(sessionId, "COMPLETED");
            } catch (Exception e) {
                updateSessionStatus(sessionId, "FAILED");
                throw new RuntimeException("Nie udało się przetworzyć pliku", e);
            }
            return CompletableFuture.completedFuture(sessionId);
        } finally {
            lockManager.releaseLock("import_process");
        }
    }


    protected void parseCsv(InputStream inputStream, ImportStatus session) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<CompletableFuture<Void>> futures = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                CompletableFuture<Void> future = processRecordAsync(record.toMap());
                futures.add(future);
            }
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            updateSessionProgress(session, csvParser.getRecordNumber());
        } catch (Exception e) {
            throw new FailedImportException("Nie udało się sparsować obiektów z pliku csv", e);
        }
    }


    public CompletableFuture<Void> processRecordAsync(Map<String, String> record) {
        return CompletableFuture.runAsync(() -> {
            String type = record.get("type");
            PersonTypeStrategy<? extends Person, ? extends PersonDto> strategy = strategyManager.getStrategy(type);
            if (strategy != null) {
                strategy.importFromCsvRecord(record);
            } else {
                throw new InvalidInputData("Nie znaleziono strategii: " + type);
            }
        });
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





