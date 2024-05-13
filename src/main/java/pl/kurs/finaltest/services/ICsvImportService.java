package pl.kurs.finaltest.services;

import pl.kurs.finaltest.database.entity.ImportStatus;

import java.io.IOException;
import java.io.InputStream;

public interface ICsvImportService {

    void parseCsv(InputStream inputStream, ImportStatus session) throws IOException;

}
