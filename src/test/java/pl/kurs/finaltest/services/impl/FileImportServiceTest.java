package pl.kurs.finaltest.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.finaltest.database.entity.ImportStatus;
import pl.kurs.finaltest.database.repositories.ImportSessionRepository;
import pl.kurs.finaltest.database.repositories.PersonRepository;
import pl.kurs.finaltest.stategy.EmployeeStrategy;
import pl.kurs.finaltest.stategy.PersonStrategyManager;
import pl.kurs.finaltest.stategy.RetireeStrategy;
import pl.kurs.finaltest.stategy.StudentStrategy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FileImportServiceTest {

    @Autowired
    private CsvImportService csvImportService;

    @Autowired
    private ImportSessionService importSessionService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ImportSessionRepository importSessionRepository;

    @Autowired
    private FileImportService fileImportService;

    @Autowired
    private RetireeStrategy retireeStrategy;

    @Autowired
    private StudentStrategy studentStrategy;

    @Autowired
    private EmployeeStrategy employeeStrategy;

    @Autowired
    private ImportQueueService importQueueService;

    @Autowired
    private ModelMapper modelMapper;

    private PersonStrategyManager strategyManager;

    @BeforeEach
    void setUp() throws IOException {
        personRepository.deleteAll();
        importSessionRepository.deleteAll();

        strategyManager = new PersonStrategyManager(
                Arrays.asList(retireeStrategy, studentStrategy, employeeStrategy)
        );
        Files.deleteIfExists(Paths.get("temp1.csv"));
        Files.deleteIfExists(Paths.get("temp2.csv"));
        Files.deleteIfExists(Paths.get("temp3.csv"));
        Files.deleteIfExists(Paths.get("temp4.csv"));
    }

    @Test
    void importFileShouldSaveObjectsToDatabase() throws Exception {
        // Given
        String csvData = "type,firstName,lastName,pesel,height,weight,emailAddress,pensionAmount,yearsWorked,universityName,yearOfStudy,fieldOfStudy,scholarshipAmount,employmentStartDate,currentPosition,currentSalary\n" +
                "retiree,John,Smith,12345678901,175,80,john.smith@example.com,2000.50,35,,,,,,,\n" +
                "student,Jane,Doe,12345678902,160,60,jane.doe@example.com,,,University,3,Computer Science,1500.75,,,\n" +
                "employee,Michael,Johnson,12345678903,180,85,michael.johnson@example.com,,,,,,,2020-01-15,Software Engineer,7500.00\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));
        String tempFilePath = "temp1.csv";
        Files.copy(inputStream, Paths.get(tempFilePath), StandardCopyOption.REPLACE_EXISTING);

        ImportStatus session = new ImportStatus();
        session.setStartTime(LocalDateTime.now());
        session.setStatus("IN_PROGRESS");
        session = importSessionRepository.save(session);

        // When
        fileImportService.importFile(tempFilePath, session.getId());

        // Upewnienie się, że metoda asynchroniczna się skończyła
        Thread.sleep(3000);

        // Then
        assertEquals(3, personRepository.count(), "Should be 3 persons in db");
        ImportStatus updatedSession = importSessionRepository.findById(session.getId()).orElseThrow();
        assertEquals("COMPLETED", updatedSession.getStatus(), "The import session should be completed");
        Files.deleteIfExists(Paths.get(tempFilePath));
    }

    @Test
    @Transactional
    void importFileShouldHandleExceptions() throws Exception {
        // Given
        String tempFilePath = "temp2.csv";
        Files.createFile(Paths.get(tempFilePath));

        ImportStatus session = new ImportStatus();
        session.setStartTime(LocalDateTime.now());
        session.setStatus("IN_PROGRESS");
        session = importSessionRepository.save(session);

        CsvImportService faultyCsvImportService = new CsvImportService(strategyManager, importSessionService) {
            @Override
            @Transactional
            public void parseCsv(InputStream inputStream, ImportStatus session) throws IOException {
                throw new IOException("Test IOException");
            }
        };

        fileImportService = new FileImportService(faultyCsvImportService, importSessionService, importQueueService);

        // When
        fileImportService.importFile(tempFilePath, session.getId());

        // Upewnienie się, że procesy asynchroniczne są zakończone
        Thread.sleep(4000);

        // Then
        assertEquals(0, personRepository.count(), "There should be no people in the database due to the exception");
        Files.deleteIfExists(Paths.get(tempFilePath));
    }

    @Test
    void importFileShouldHandleMultipleRequestsInQueue() throws Exception {
        // Given
        String csvData1 = "type,firstName,lastName,pesel,height,weight,emailAddress,pensionAmount,yearsWorked,universityName,yearOfStudy,fieldOfStudy,scholarshipAmount,employmentStartDate,currentPosition,currentSalary\n" +
                "retiree,John,Smith,12345678901,175,80,john.smith@example.com,2000.50,35,,,,,,,\n";
        String csvData2 = "type,firstName,lastName,pesel,height,weight,emailAddress,pensionAmount,yearsWorked,universityName,yearOfStudy,fieldOfStudy,scholarshipAmount,employmentStartDate,currentPosition,currentSalary\n" +
                "student,Jane,Doe,12345678902,160,60,jane.doe@example.com,,,University,3,Computer Science,1500.75,,,\n";
        ByteArrayInputStream inputStream1 = new ByteArrayInputStream(csvData1.getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream inputStream2 = new ByteArrayInputStream(csvData2.getBytes(StandardCharsets.UTF_8));
        String tempFilePath1 = "temp3.csv";
        String tempFilePath2 = "temp4.csv";
        Files.copy(inputStream1, Paths.get(tempFilePath1), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(inputStream2, Paths.get(tempFilePath2), StandardCopyOption.REPLACE_EXISTING);

        ImportStatus session1 = new ImportStatus();
        session1.setStartTime(LocalDateTime.now());
        session1.setStatus("IN_PROGRESS");
        session1 = importSessionRepository.save(session1);

        ImportStatus session2 = new ImportStatus();
        session2.setStartTime(LocalDateTime.now());
        session2.setStatus("IN_PROGRESS");
        session2 = importSessionRepository.save(session2);

        // When
        fileImportService.importFile(tempFilePath1, session1.getId());
        fileImportService.importFile(tempFilePath2, session2.getId());

        // Upewnienie się, że metoda asynchroniczna się skończyła
        Thread.sleep(6000);

        // Then
        assertEquals(2, personRepository.count(), "Powinny być 2 osoby w db");
        ImportStatus updatedSession1 = importSessionRepository.findById(session1.getId()).orElseThrow();
        ImportStatus updatedSession2 = importSessionRepository.findById(session2.getId()).orElseThrow();
        assertEquals("COMPLETED", updatedSession1.getStatus(), "The first import session should be completed");
        assertEquals("COMPLETED", updatedSession2.getStatus(), "The second import session should be completed");
        Files.deleteIfExists(Paths.get(tempFilePath1));
        Files.deleteIfExists(Paths.get(tempFilePath2));
    }
}