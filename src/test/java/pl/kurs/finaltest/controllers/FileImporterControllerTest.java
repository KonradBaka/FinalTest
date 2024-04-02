package pl.kurs.finaltest.controllers;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.finaltest.services.FileImportService;

import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class FileImporterControllerTest {

    @Mock
    private FileImportService fileImportService;

    @InjectMocks
    private FileImporterController fileImporterController;

    @Test
    public void shouldImportFileSuccessfully() throws Exception {
        // given
        MultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());


        // when
        ResponseEntity<?> response = fileImporterController.importCsv(file);

        // then
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Import w toku", response.getBody());

        verify(fileImportService, times(1)).importFile(file);
    }
}