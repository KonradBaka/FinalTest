package pl.kurs.finaltest.services;

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

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class FileImportService implements IFileImportService {

    private AtomicBoolean importInProgress = new AtomicBoolean(false);

    private CsvParserService csvParserService;
    private PersonStrategyManager strategyManager;
    private ImportSessionRepository importSessionRepository;

    public FileImportService(CsvParserService csvParserService, PersonStrategyManager strategyManager, ImportSessionRepository importSessionRepository) {
        this.csvParserService = csvParserService;
        this.strategyManager = strategyManager;
        this.importSessionRepository = importSessionRepository;
    }


    @Async
    @Transactional
    public Long importFile(MultipartFile file) throws ImportInProgressException {

        //Tego wcześniej zapomniałem dodać
        if (!importInProgress.compareAndSet(false, true)) {
            throw new ImportInProgressException("Inny import jest przetwarzany.");
        }

        final ImportStatus session = new ImportStatus();
        session.setStartTime(LocalDateTime.now());
        session.setStatus("IN_PROGRESS");
        ImportStatus savedSession = importSessionRepository.save(session);

        // Do wykonywania asynchronicznego
        CompletableFuture.runAsync(() -> processFileAsync(file, savedSession))
                .thenRun(() -> importInProgress.set(false));

        return savedSession.getId();
    }

    private void processFileAsync(MultipartFile file, ImportStatus session) {
        try {
            processFile(file, session);
            updateSessionStatus(session, "COMPLETED");
        } catch (Exception e) {
            updateSessionStatus(session, "FAILED");
        } finally {
            importInProgress.set(false);
        }
    }

    private void processFile(MultipartFile file, ImportStatus session) {
        try {
            // Optymalizacja przez pobieranie recodrów pojedynczo
            csvParserService.parseCsv(file.getInputStream(), record -> {
                String type = record.get("TYPE");
                if (type != null) {
                    TypeIdentifierDto typeIdentifierDto = new TypeIdentifierDto(type);
                    PersonTypeStrategy<? extends Person, ? extends PersonDto> strategy = strategyManager.getStrategy(typeIdentifierDto);
                    if (strategy != null) {
                        strategy.importFromCsvRecord(record);
                        synchronized (session) {
                            session.incrementRecordsProcessed();
                        }
                    } else {
                        throw new RuntimeException("Strategia nie znaleziona: " + type);
                    }
                } else {
                    throw new RuntimeException("Nie znaleziono typu");
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Błąd podczas przetwarzania pliku", e);
        }
    }

    @Transactional
    public void updateSessionStatus(ImportStatus session, String status) {
        session.setStatus(status);
        session.setEndTime(LocalDateTime.now());
        importSessionRepository.save(session);
    }
}


