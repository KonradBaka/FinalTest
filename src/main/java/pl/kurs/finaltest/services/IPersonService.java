package pl.kurs.finaltest.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.kurs.finaltest.criteria.PersonCriteria;
import pl.kurs.finaltest.dto.PersonDto;

public interface IPersonService {

    <T extends PersonDto> T addPerson(T personDto);
    <T extends PersonDto> T editPerson(Long id, T personDto);
    public <T extends PersonCriteria, U extends PersonDto> Page<U> findPersons(T criteria, Pageable pageable);
}
