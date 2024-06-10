package pl.kurs.finaltest.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import pl.kurs.finaltest.dto.StatusDto;
import pl.kurs.finaltest.services.impl.FileImportService;
import pl.kurs.finaltest.services.impl.ImportSessionService;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestPropertySource(locations = "classpath:application-test.properties")
public class FileImporterControllerTest {

    @InjectMocks
    private FileImporterController fileImporterController;

    @Mock
    private FileImportService fileImportService;

    @Mock
    private ImportSessionService importSessionService;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void importCsvShouldReturnStatusOk() throws Exception {
        // Given
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                "type,firstName,lastName,pesel,height,weight,emailAddress,pensionAmount,yearsWorked\nretiree,John,Smith,12345678901,175,80,john.smith@example.com,2000.50,35".getBytes()
        );

        when(fileImportService.initiateImportSession()).thenReturn(1L);
        doNothing().when(fileImportService).importFile(anyString(), eq(1L));

        // When
        ResponseEntity<StatusDto> response = fileImporterController.importCsv(file);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("1", response.getBody().getStatus());

        verify(fileImportService, times(1)).initiateImportSession();
        verify(fileImportService, times(1)).importFile(anyString(), eq(1L));
    }
}


