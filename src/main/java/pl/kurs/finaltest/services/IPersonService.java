package pl.kurs.finaltest.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import pl.kurs.finaltest.criteria.PersonCriteria;
import pl.kurs.finaltest.dto.PersonDto;
import pl.kurs.finaltest.models.Person;

public interface IPersonService {

    <T extends PersonDto> T addPerson(T personDto);
    <T extends PersonDto> T editPerson(Long id, T personDto);
    <T extends PersonCriteria> Page<PersonDto> findPersons(T criteria, Pageable pageable);
}
