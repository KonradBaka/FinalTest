package pl.kurs.finaltest.services;

import pl.kurs.finaltest.dto.PersonDto;
import pl.kurs.finaltest.models.Person;

import java.util.Map;

public interface PersonTypeStrategy {

    boolean supports(PersonDto personDto);
    Person addPerson(PersonDto personDto);
    Person editPerson(Long id, PersonDto personDTO);
    Person importFromCsvRecord(Map<String, String> csvRecord);

}
