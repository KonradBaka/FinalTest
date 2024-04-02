package pl.kurs.finaltest.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface IFileImportService {
    CompletableFuture<String> importFile(MultipartFile file) throws IOException;
}
