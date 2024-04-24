package pl.kurs.finaltest.services;

import org.springframework.web.multipart.MultipartFile;
import pl.kurs.finaltest.models.ImportStatus;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface IFileImportService {

    CompletableFuture<Long> importFile(Long sessionId, MultipartFile file) throws IOException, InterruptedException;

}
