package pl.kurs.finaltest.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileImportService {
    Long importFile(MultipartFile file) throws IOException;
}
