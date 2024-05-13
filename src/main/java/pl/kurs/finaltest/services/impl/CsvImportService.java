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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CsvImportService implements ICsvImportService {

    private PersonStrategyManager strategyManager;
    private ImportSessionService importSessionService;

    public CsvImportService(PersonStrategyManager strategyManager, ImportSessionService importSessionService) {
        this.strategyManager = strategyManager;
        this.importSessionService = importSessionService;
    }

    @Override
    public void parseCsv(InputStream inputStream, ImportStatus session) throws IOException {

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            for (CSVRecord record : csvParser) {
                processRecord(record.toMap());
                importSessionService.incrementRecordsProcessed(session.getId());
            }
        }
    }

    private void processRecord(Map<String, String> record) {
        String type = record.get("type");
        PersonTypeStrategy<? extends Person, ? extends PersonDto> strategy = strategyManager.getStrategy(type);
        strategy.importFromCsvRecord(record);
    }
}
