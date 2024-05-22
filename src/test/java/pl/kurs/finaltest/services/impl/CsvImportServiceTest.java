package pl.kurs.finaltest.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.finaltest.database.entity.ImportStatus;
import pl.kurs.finaltest.database.repositories.PersonRepository;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class CsvImportServiceTest {


    @Autowired
    private CsvImportService csvImportService;

    @Autowired
    private ImportSessionService importSessionService;

    @Autowired
    private PersonRepository personRepository;

    private Long sessionId;
    private ImportStatus session;

    @BeforeEach
    void setUp() {
        sessionId = importSessionService.createImportSession();
        session = importSessionService.getImportStatus(sessionId);
    }

    @Test
    @Transactional
    void parseCsvShouldBeTransactional() {
        // Given
        String csvData = "type,firstName,lastName,pesel,height,weight,emailAddress,pensionAmount,yearsWorked\n" +
                "retiree,John,Smith,12345678901,175,80,john.smith@example.com,2000.50,35\n" +
                "retiree,Jane,Doe,12345678902,160,60,jane.doe@example.com,1500.75,40\n" +
                "retiree,Michael,Johnson,12345678903,180,85,michael.johnson@example.com,1800.00,30\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));

        // When & Then
        assertThatThrownBy(() -> {
            csvImportService.parseCsv(inputStream, session);
            throw new RuntimeException("error");
        }).isInstanceOf(RuntimeException.class);

        long count = personRepository.count();
        assertEquals(0, count, "Powinien być rollback");
    }
}