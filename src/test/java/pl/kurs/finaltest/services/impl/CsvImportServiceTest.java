package pl.kurs.finaltest.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class CsvImportServiceTest {


    @Autowired
    private CsvImportService csvImportService;

    @Autowired
    private ImportSessionService importSessionService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ImportSessionRepository importSessionRepository;

    @Autowired
    private RetireeStrategy retireeStrategy;

    @Autowired
    private ModelMapper modelMapper;

    private PersonStrategyManager strategyManager;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        personRepository.deleteAll();
        importSessionRepository.deleteAll();

        strategyManager = new PersonStrategyManager(Collections.singletonList(retireeStrategy));
        Files.deleteIfExists(Paths.get("temp.csv"));
    }

    @Test
    @Transactional
    void parseCsvShouldImportDataCorrectly() throws Exception {
        // Given
        String csvData = "type,firstName,lastName,pesel,height,weight,emailAddress,pensionAmount,yearsWorked\n" +
                "retiree,John,Smith,12345678901,175,80,john.smith@example.com,2000.50,35\n" +
                "retiree,Jane,Doe,12345678902,160,60,jane.doe@example.com,1500.75,40\n" +
                "retiree,Michael,Johnson,12345678903,180,85,michael.johnson@example.com,1800.00,30\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));

        ImportStatus session = new ImportStatus();
        session.setStartTime(LocalDateTime.now());
        session.setStatus("IN_PROGRESS");
        session = importSessionRepository.save(session);

        // When
        csvImportService.parseCsv(inputStream, session);

        // Then
        assertEquals(3, personRepository.count(), "Powinny być 3 osoby w db");
        ImportStatus updatedSession = importSessionRepository.findById(session.getId()).orElseThrow();
        assertEquals("IN_PROGRESS", updatedSession.getStatus());
        assertNotNull(personRepository.findByPesel("12345678901"), "John Smith jest");
        assertNotNull(personRepository.findByPesel("12345678902"), "Jane Doe jest");
        assertNotNull(personRepository.findByPesel("12345678903"), "Michael jest");
    }

    @Test
    @Transactional
    void parseCsvShouldHandleEmptyInputStream() throws Exception {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8));

        ImportStatus session = new ImportStatus();
        session.setStartTime(LocalDateTime.now());
        session.setStatus("IN_PROGRESS");
        session = importSessionRepository.save(session);

        // When
        csvImportService.parseCsv(inputStream, session);

        // Then
        assertEquals(0, personRepository.count(), "Nie powinno byc ludzi w db");
        ImportStatus updatedSession = importSessionRepository.findById(session.getId()).orElseThrow();
    }

    @Test
    void parseCsvShouldRollbackOnFailure() throws Exception {
        // Given
        String csvData = "type,firstName,lastName,pesel,height,weight,emailAddress,pensionAmount,yearsWorked\n" +
                "retiree,John,Smith,12345678901,175,80,john.smith@example.com,2000.50,35\n" +
                "retiree,Jane,Doe,INVALID_PESEL,160,60,jane.doe@example.com,1500.75,40\n" + // INVALID_PESEL to simulate an error
                "retiree,Michael,Johnson,12345678903,180,85,michael.johnson@example.com,1800.00,30\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));

        ImportStatus session = new ImportStatus();
        session.setStartTime(LocalDateTime.now());
        session.setStatus("IN_PROGRESS");
        session = importSessionRepository.save(session);

        assertEquals(0, personRepository.count(), "Db powinnop byc puste przed testem metody");

        // When
        Exception exception = null;
        try {
            csvImportService.parseCsv(inputStream, session);
        } catch (Exception e) {
            exception = e;
        }

        // Then
        assertNotNull(exception, "Powinien rzucuć wyjątek związany z niepoprawnym PESELem");

        assertEquals(0, personRepository.count(), "Nie powinno byc ludzi w db dzięki rollbackowi");
        ImportStatus updatedSession = importSessionRepository.findById(session.getId()).orElseThrow();
    }
}