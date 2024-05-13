package pl.kurs.finaltest.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface IFileImportService {

    CompletableFuture<Long> importFile(MultipartFile file) throws ExecutionException, InterruptedException;

}
