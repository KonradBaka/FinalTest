package pl.kurs.finaltest.controllers;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class FileImporterControllerTest {

//    @Mock
//    private FileImportService fileImportService;
//
//    @InjectMocks
//    private FileImporterController fileImporterController;
//
//    @Test
//    public void shouldImportFileSuccessfully() throws Exception {
//        // given
//        MultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
//
//
//        // when
//        ResponseEntity<?> response = fileImporterController.importCsv(file);
//
//        // then
//        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
//        assertEquals("Import w toku", response.getBody());
//
//        verify(fileImportService, times(1)).importFile(file);
//    }
}