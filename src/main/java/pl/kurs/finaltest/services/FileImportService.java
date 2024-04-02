package pl.kurs.finaltest.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.finaltest.dto.TypeIdentifierDto;
import pl.kurs.finaltest.models.ImportStatus;
import pl.kurs.finaltest.repositories.ImportSessionRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class FileImportService implements IFileImportService {

    private CsvParserService csvParserService;
    private PersonStrategyManager strategyManager;
    private ImportSessionRepository importSessionRepository;

    public FileImportService(CsvParserService csvParserService, PersonStrategyManager strategyManager, ImportSessionRepository importSessionRepository) {
        this.csvParserService = csvParserService;
        this.strategyManager = strategyManager;
        this.importSessionRepository = importSessionRepository;
    }

    @Async
    public CompletableFuture<String> importFile(MultipartFile file) throws IOException {
        ImportStatus session = new ImportStatus();
        session.setStartTime(LocalDateTime.now());
        session.setStatus("IN_PROGRESS");
        session = importSessionRepository.save(session);

        try {
            List<Map<String, String>> records = csvParserService.parseCsv(file.getInputStream());

            for (Map<String, String> record : records) {
                String type = record.get("TYPE");
                if (type != null) {
                    TypeIdentifierDto typeIdentifierDto = new TypeIdentifierDto(type);
                    try {
                        PersonTypeStrategy strategy = strategyManager.getStrategy(typeIdentifierDto);
                        strategy.importFromCsvRecord(record);
                    } catch (Exception e) {
                        throw new RuntimeException("Błąd");
                    }
                } else {
                    throw new RuntimeException("Nie znaleziono typu");
                }
            }

            session.setEndTime(LocalDateTime.now());
            session.setStatus("COMPLETED");
            importSessionRepository.save(session);
        } catch (Exception e) {
            session.setStatus("FAILED");
        } finally {
            session.setEndTime(LocalDateTime.now());
            importSessionRepository.save(session);
        }
        return CompletableFuture.completedFuture(session.getStatus().equals("COMPLETED") ? "Import udany" : "Import nieudany");
    }
}
