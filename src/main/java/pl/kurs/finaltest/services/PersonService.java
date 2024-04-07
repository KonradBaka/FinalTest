package pl.kurs.finaltest.services;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.finaltest.criteria.PersonCriteria;
import pl.kurs.finaltest.dto.PersonDto;
import pl.kurs.finaltest.entityspecification.SpecificationManager;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.repositories.PersonRepository;

@Service
@Transactional
public class PersonService implements IPersonService {

    private final PersonRepository personRepository;
    private final PersonStrategyManager strategyManager;
    private final SpecificationManager specificationManager;
    private final ModelMapper modelMapper;

    public PersonService(PersonRepository personRepository, PersonStrategyManager strategyManager, SpecificationManager specificationManager, ModelMapper modelMapper) {
        this.personRepository = personRepository;
        this.strategyManager = strategyManager;
        this.specificationManager = specificationManager;
        this.modelMapper = modelMapper;
    }

    @Override
    public <T extends PersonDto> T addPerson(T personDto) {
        @SuppressWarnings("unchecked")
        PersonTypeStrategy<? extends Person, T> strategy = (PersonTypeStrategy<? extends Person, T>) strategyManager.getStrategy(personDto);

        if (strategy == null || !strategy.supports(personDto)) {
            throw new IllegalArgumentException("Nie znaleziono strategii dla podanego typu: " + personDto.getType());
        }

        Person person = strategy.addPerson(personDto);
        return modelMapper.map(person, (Class<T>) personDto.getClass());
    }

    @Override
    public <T extends PersonDto> T editPerson(Long id, T personDto) {
        PersonTypeStrategy<? extends Person, ? extends PersonDto> strategy = strategyManager.getStrategy(personDto);

        if (strategy == null) {
            throw new IllegalArgumentException("Nie znaleziono strategii dla podanego typu: " + personDto.getType());
        }

        if (!strategy.supports(personDto)) {
            throw new IllegalArgumentException("Strategia nie obsługuje podanego typu DTO: " + personDto.getClass().getName());
        }

        @SuppressWarnings("unchecked")
        PersonTypeStrategy<? extends Person, T> castedStrategy = (PersonTypeStrategy<? extends Person, T>) strategy;
        Person person = castedStrategy.editPerson(id, personDto);
        return modelMapper.map(person, (Class<T>) personDto.getClass());
    }

    @Override
    public <T extends PersonCriteria> Page<PersonDto> findPersons(T criteria, Pageable pageable) {
        Specification<Person> spec = specificationManager.getSpecification(criteria);
        Page<Person> persons = personRepository.findAll(spec, pageable);
        return persons.map(person -> modelMapper.map(person, PersonDto.class));
    }
}

