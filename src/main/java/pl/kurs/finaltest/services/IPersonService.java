package pl.kurs.finaltest.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.kurs.finaltest.dto.PersonDto;
import pl.kurs.finaltest.entityspecification.SearchCriteria;
import pl.kurs.finaltest.models.Person;

public interface IPersonService {

    Person addPerson(PersonDto personDto);
    Person editPerson(Long id, PersonDto personDto);
    Page<Person> findPersons(SearchCriteria searchCriteria, Pageable pageable);
}
