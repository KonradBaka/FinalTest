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

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/import")
public class FileImporterController {

    private FileImportService fileImportService;
    private ImportSessionRepository importSessionRepository;
    private ModelMapper modelMapper;

    public FileImporterController(FileImportService fileImportService, ImportSessionRepository importSessionRepository, ModelMapper modelMapper) {
        this.fileImportService = fileImportService;
        this.importSessionRepository = importSessionRepository;
        this.modelMapper = modelMapper;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "application/json")
    public CompletableFuture<ResponseEntity<Long>> importCsv(@RequestParam("file") MultipartFile file) throws InterruptedException { // Pisałeś żebym zwracał tylko Long ale jak zwrócimy ID natychmiast po odpaleniu ?
        Long sessionId = fileImportService.createNewImportSession();
        return fileImportService.importFile(sessionId, file)
                .thenApply(sId -> ResponseEntity.ok(sId));
    }


    @GetMapping(produces = "application/json")
    public ResponseEntity<List<ImportStatusDto>> getImportStatus() {
            List<ImportStatus> sessions = importSessionRepository.findAll();
            List<ImportStatusDto> dtos = sessions.stream()
                    .map(session -> modelMapper.map(session, ImportStatusDto.class))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
    }
}
