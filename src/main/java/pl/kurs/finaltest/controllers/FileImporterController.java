package pl.kurs.finaltest.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.finaltest.dto.ImportStatusDto;
import pl.kurs.finaltest.dto.StatusDto;
import pl.kurs.finaltest.exceptions.ImportInProgressException;
import pl.kurs.finaltest.models.ImportStatus;
import pl.kurs.finaltest.repositories.ImportSessionRepository;
import pl.kurs.finaltest.services.FileImportService;

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
    public ResponseEntity<Long> importCsv(@RequestParam("file") MultipartFile file) {
        try {
            Long sessionId = fileImportService.importFile(file);
            return ResponseEntity.ok(sessionId);
        } catch (ImportInProgressException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/status/{sessionId}")
    public ResponseEntity<ImportStatusDto> getImportStatus(@PathVariable Long sessionId) {
        ImportStatus session = importSessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono sesji o ID: " + sessionId));

        ImportStatusDto dto = modelMapper.map(session, ImportStatusDto.class);
        return ResponseEntity.ok(dto);
    }
}
