package pl.kurs.finaltest.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import pl.kurs.finaltest.exceptions.InvalidInputData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class CsvParserService {

    public void parseCsv(InputStream inputStream, Consumer<Map<String, String>> recordConsumer) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord record : csvParser) {
                recordConsumer.accept(record.toMap());
            }
        } catch (Exception e) {
            throw new InvalidInputData("Niewłaściwy format pliku");
        }
    }
}
