package pl.kurs.finaltest.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import pl.kurs.finaltest.database.entity.ImportStatus;
import pl.kurs.finaltest.database.repositories.PersonRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class FileImportServiceTest {

    @Autowired
    private FileImportService fileImportService;

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
    void testImportFileTransactionality() throws IOException {
        // Given
        String csvData = "type,firstName,lastName,pesel,height,weight,emailAddress,pensionAmount,yearsWorked\n" +
                "retiree,John,Smith,12345678901,175,80,john.smith@example.com,2000.50,35\n" +
                "retiree,Jane,Doe,12345678902,160,60,jane.doe@example.com,1500.75,40\n" +
                "retiree,Michael,Johnson,12345678903,180,85,michael.johnson@example.com,1800.00,30\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));
        String tempFilePath = "C:\\Users\\user\\Documents\\final-test\\retiree-test.csv";

        java.nio.file.Files.copy(inputStream, java.nio.file.Paths.get(tempFilePath), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        // When & Then
        assertThatThrownBy(() -> {
            fileImportService.importFile(tempFilePath, sessionId);
            throw new RuntimeException("error");
        }).isInstanceOf(RuntimeException.class);

        long count = personRepository.count();
        assertEquals(0, count, "Powinien być rollback");
    }


}