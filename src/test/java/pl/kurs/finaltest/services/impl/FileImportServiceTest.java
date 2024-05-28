package pl.kurs.finaltest.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.finaltest.database.entity.ImportStatus;
import pl.kurs.finaltest.database.repositories.ImportSessionRepository;
import pl.kurs.finaltest.database.repositories.PersonRepository;
import pl.kurs.finaltest.stategy.PersonStrategyManager;
import pl.kurs.finaltest.stategy.RetireeStrategy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class FileImportServiceTest {
    @Autowired
    private CsvImportService csvImportService;

    @Autowired
    private ImportSessionService importSessionService;

    @Autowired
    private LockManagerService lockManagerService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ImportSessionRepository importSessionRepository;

    @Autowired
    private FileImportService fileImportService;

    @Autowired
    private RetireeStrategy retireeStrategy;

    @Autowired
    private ModelMapper modelMapper;

    private PersonStrategyManager strategyManager;

    @BeforeEach
    void setUp() throws IOException {
        personRepository.deleteAll();
        importSessionRepository.deleteAll();

        strategyManager = new PersonStrategyManager(Collections.singletonList(retireeStrategy));
        Files.deleteIfExists(Paths.get("temp.csv"));
    }


    @Test
    void importFileShouldSaveObjectsToDatabase() throws Exception {
        // Given
        String csvData = "type,firstName,lastName,pesel,height,weight,emailAddress,pensionAmount,yearsWorked\n" +
                "retiree,John,Smith,12345678901,175,80,john.smith@example.com,2000.50,35\n" +
                "retiree,Jane,Doe,12345678902,160,60,jane.doe@example.com,1500.75,40\n" +
                "retiree,Michael,Johnson,12345678903,180,85,michael.johnson@example.com,1800.00,30\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));
        String tempFilePath = "temp.csv";
        Files.copy(inputStream, Paths.get(tempFilePath), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        ImportStatus session = new ImportStatus();
        session.setStartTime(LocalDateTime.now());
        session.setStatus("IN_PROGRESS");
        session = importSessionRepository.save(session);


        // When
        fileImportService.importFile(tempFilePath, session.getId());

        // upewnienie sie że metoda asynchroniczna sie skończyła
        Thread.sleep(1000);

        // Then
        assertEquals(3, personRepository.count(), "Powinny być 3 osoby w db");
        Files.deleteIfExists(Paths.get(tempFilePath));
    }

    @Test
    @Transactional
    void importFileShouldHandleExceptions() throws Exception {
        // Given
        String tempFilePath = "temp.csv";
        Files.createFile(Paths.get(tempFilePath));

        ImportStatus session = new ImportStatus();
        session.setStartTime(LocalDateTime.now());
        session.setStatus("IN_PROGRESS");
        session = importSessionRepository.save(session);

        assertTrue(lockManagerService.acquireLock("import_process"));

        CsvImportService faultyCsvImportService = new CsvImportService(strategyManager, importSessionService) {
            @Override
            @Transactional
            public void parseCsv(InputStream inputStream, ImportStatus session) throws IOException {
                throw new IOException("Test IOException");
            }
        };

        fileImportService = new FileImportService(faultyCsvImportService, importSessionService, lockManagerService);

        // When
        fileImportService.importFile(tempFilePath, session.getId());

        // Upewnienie sie ze procesy asynchroniczne sa zakonczone
        Thread.sleep(1000);

        // Then
        assertEquals(0, personRepository.count(), "There should be no people in the database due to the exception");
        ImportStatus updatedSession = importSessionRepository.findById(session.getId()).orElseThrow();
        assertEquals("FAILED", updatedSession.getStatus(), "The import session should be failed");
        Files.deleteIfExists(Paths.get(tempFilePath));
    }
}