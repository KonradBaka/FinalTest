package pl.kurs.finaltest.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import pl.kurs.finaltest.database.entity.Person;
import pl.kurs.finaltest.dto.PersonDto;

public interface IPersonService {

    <T extends PersonDto> T addPerson(T personDto);
    <T extends PersonDto> T editPerson(Long id, T personDto);
    Page<Person> searchPeople(Specification<Person> spec, Pageable pageable);
}
