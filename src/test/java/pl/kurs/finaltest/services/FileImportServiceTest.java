package pl.kurs.finaltest.services;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FileImportServiceTest {

//    @Mock
//    private CsvParserService csvParserService;
//
//
//    @InjectMocks
//    private FileImportService fileImportService;
//
//    @Test
//    void shouldImportFileSuccessfully() throws Exception {
//        // Given
//        MultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "header1,header2\nvalue1,value2".getBytes());
//        List<Map<String, String>> parsedRecords = List.of(Map.of("header1", "value1", "header2", "value2"));
//        when(csvParserService.parseCsv(file.getInputStream())).thenReturn(parsedRecords);
//
//
//        // When
//        fileImportService.importFile(file);
//
//        // Then
//        verify(csvParserService, times(1)).parseCsv(any(InputStream.class));
//
//    }
}