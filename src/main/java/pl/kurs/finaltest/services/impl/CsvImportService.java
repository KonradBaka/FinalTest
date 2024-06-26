package pl.kurs.finaltest.services.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.finaltest.database.entity.ImportStatus;
import pl.kurs.finaltest.database.entity.Person;
import pl.kurs.finaltest.dto.PersonDto;
import pl.kurs.finaltest.services.ICsvImportService;
import pl.kurs.finaltest.stategy.PersonStrategyManager;
import pl.kurs.finaltest.stategy.PersonTypeStrategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
public class CsvImportService implements ICsvImportService {

    private PersonStrategyManager strategyManager;
    private ImportSessionService importSessionService;

    public CsvImportService(PersonStrategyManager strategyManager, ImportSessionService importSessionService) {
        this.strategyManager = strategyManager;
        this.importSessionService = importSessionService;
    }


    @Override
    @Transactional
    public void parseCsv(InputStream inputStream, ImportStatus session) throws IOException {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            int recordCount = 0;

            for (CSVRecord record : csvParser) {
                String type = record.get("type");
                PersonTypeStrategy<? extends Person, ? extends PersonDto> strategy = strategyManager.getStrategy(type);
                strategy.importFromCsvRecord(record.toMap());

                recordCount++;
                if (recordCount % 10000 == 0) {
                    importSessionService.incrementRecordsProcessed(session.getId(), 10000);
                    recordCount = 0;
                }
            }
            if (recordCount > 0) {
                importSessionService.incrementRecordsProcessed(session.getId(), recordCount);
            }
        }
    }
}