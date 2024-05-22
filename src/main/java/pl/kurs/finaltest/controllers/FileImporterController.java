package pl.kurs.finaltest.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.finaltest.database.entity.ImportStatus;
import pl.kurs.finaltest.dto.ImportStatusDto;
import pl.kurs.finaltest.services.impl.FileImportService;
import pl.kurs.finaltest.services.impl.ImportSessionService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/import")
public class FileImporterController {

    private FileImportService fileImportService;
    private ImportSessionService importSessionService;
    private ModelMapper modelMapper;

    public FileImporterController(FileImportService fileImportService, ImportSessionService importSessionService, ModelMapper modelMapper) {
        this.fileImportService = fileImportService;
        this.importSessionService = importSessionService;
        this.modelMapper = modelMapper;
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "application/json")
    public ResponseEntity<Long> importCsv(@RequestParam("file") MultipartFile file) {
        try {

            String path = "C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\final-test\\";
            Path tempFile = Files.createTempFile(Paths.get(path), "upload_", ".tmp");
            Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

            Long sessionId = fileImportService.initiateImportSession();
            fileImportService.importFile(tempFile.toString(), sessionId);

            return ResponseEntity.ok(sessionId);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "application/json")
//    public ResponseEntity<Long> importCsv(@RequestParam("file") MultipartFile file) {
//        Long sessionId = fileImportService.initiateImportSession();
//        fileImportService.importFile(file, sessionId);
//        return ResponseEntity.ok(sessionId);
//    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<ImportStatusDto>> getImportStatus() {
        List<ImportStatus> allSessions = importSessionService.getAllSessions();
        List<ImportStatusDto> dtos = allSessions.stream()
                .map(session -> modelMapper.map(session, ImportStatusDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
