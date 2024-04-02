package pl.kurs.finaltest.services;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.finaltest.dto.PersonDto;
import pl.kurs.finaltest.entityspecification.SearchCriteria;
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

    public Person addPerson(PersonDto personDto) {
        PersonTypeStrategy strategy = strategyManager.getStrategy(personDto);
        return strategy.addPerson(personDto);
    }

    public Person editPerson(Long id, PersonDto personDto) {
        PersonTypeStrategy strategy = strategyManager.getStrategy(personDto);
        return strategy.editPerson(id, personDto);
    }

    public Page<Person> findPersons(SearchCriteria searchCriteria, Pageable pageable) {
        Specification<Person> spec = buildSpecificationFromCriteria(searchCriteria);
        return personRepository.findAll(spec, pageable);
    }

    private Specification<Person> buildSpecificationFromCriteria(SearchCriteria searchCriteria) {
        Specification<Person> typeSpec = specificationManager.getSpecification(searchCriteria.getType(), searchCriteria);
        return Specification.where(typeSpec);
    }
}

