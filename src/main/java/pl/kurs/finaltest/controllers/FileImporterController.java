package pl.kurs.finaltest.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.finaltest.database.entity.ImportStatus;
import pl.kurs.finaltest.dto.ImportStatusDto;
import pl.kurs.finaltest.dto.StatusDto;
import pl.kurs.finaltest.services.impl.FileImportService;
import pl.kurs.finaltest.services.impl.ImportSessionService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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


    @PostMapping
    public ResponseEntity<StatusDto> importCsv(@RequestParam("file") MultipartFile file) throws IOException {

        String userHome = System.getProperty("user.home");
        Path path = Paths.get(userHome, "Documents", "final-test");
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        Path tempFile = Files.createTempFile(path, "upload_", ".tmp");
        Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

        Long sessionId = fileImportService.initiateImportSession();
        fileImportService.importFile(tempFile.toString(), sessionId);

        return ResponseEntity.ok(new StatusDto(sessionId.toString()));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ImportStatusDto> getImportStatus(@PathVariable Long id) {
        ImportStatus importStatus = importSessionService.getImportStatus(id);
        ImportStatusDto importStatusDto = modelMapper.map(importStatus, ImportStatusDto.class);
        return ResponseEntity.ok(importStatusDto);
    }
}
