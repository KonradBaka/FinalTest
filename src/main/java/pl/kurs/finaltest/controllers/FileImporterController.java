package pl.kurs.finaltest.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.finaltest.dto.ImportStatusDto;
import pl.kurs.finaltest.models.ImportStatus;
import pl.kurs.finaltest.repositories.ImportSessionRepository;
import pl.kurs.finaltest.services.FileImportService;

import java.io.IOException;

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

    @PostMapping
    public ResponseEntity<?> importCsv(@RequestParam("file") MultipartFile file) throws IOException {
        fileImportService.importFile(file);
        return ResponseEntity.accepted().body("Import w toku");
    }

    @GetMapping("/status")
    public ResponseEntity<ImportStatusDto> getImportStatus() {
        ImportStatus latestSession = importSessionRepository.findTopByOrderByStartTimeDesc()
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono sesji."));

        ImportStatusDto dto = modelMapper.map(latestSession, ImportStatusDto.class);
        return ResponseEntity.ok(dto);
    }
}
