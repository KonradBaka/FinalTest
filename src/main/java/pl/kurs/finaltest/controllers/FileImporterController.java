package pl.kurs.finaltest.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pl.kurs.finaltest.dto.ImportStatusDto;
import pl.kurs.finaltest.database.entity.ImportStatus;
import pl.kurs.finaltest.database.repositories.ImportSessionRepository;
import pl.kurs.finaltest.exceptions.ImportInProgressException;
import pl.kurs.finaltest.services.impl.FileImportService;
import pl.kurs.finaltest.services.impl.ImportSessionService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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
        Long sessionId = fileImportService.initiateImportSession();
        fileImportService.importFile(file, sessionId);
        return ResponseEntity.ok(sessionId);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<ImportStatusDto>> getImportStatus() {
        List<ImportStatus> allSessions = importSessionService.getAllSessions();
        List<ImportStatusDto> dtos = allSessions.stream()
                .map(session -> modelMapper.map(session, ImportStatusDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
