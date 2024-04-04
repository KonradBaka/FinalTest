package pl.kurs.finaltest.services;

import pl.kurs.finaltest.dto.PersonDto;
import pl.kurs.finaltest.models.Person;

import java.util.Map;

public interface PersonTypeStrategy<T extends Person, D extends PersonDto> {
    Class<D> getHandledDtoClass();

    boolean supports(PersonDto personDto);

    T addPerson(D personDto);

    T editPerson(Long id, D personDto);

    T importFromCsvRecord(Map<String, String> csvRecord);
}
