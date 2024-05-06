package pl.kurs.finaltest.services;

import pl.kurs.finaltest.dto.PersonDto;
import pl.kurs.finaltest.database.entity.Person;

import java.util.Map;

public interface PersonTypeStrategy<T extends Person, D extends PersonDto> {
    String getHandledType();
    boolean supports(PersonDto personDto);

    T addPerson(D personDto);

    T editPerson(Long id, D personDto);

    T importFromCsvRecord(Map<String, String> csvRecord);
}
