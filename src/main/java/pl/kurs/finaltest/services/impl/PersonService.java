package pl.kurs.finaltest.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.finaltest.database.entity.Employee;
import pl.kurs.finaltest.database.entity.Person;
import pl.kurs.finaltest.database.repositories.PersonRepository;
import pl.kurs.finaltest.database.repositories.PositionRepository;
import pl.kurs.finaltest.dto.EmployeeDto;
import pl.kurs.finaltest.dto.PersonDto;
import pl.kurs.finaltest.exceptions.InvalidInputData;
import pl.kurs.finaltest.services.IPersonService;
import pl.kurs.finaltest.stategy.PersonTypeStrategy;
import pl.kurs.finaltest.stategy.PersonStrategyManager;

@Service
@Transactional
public class PersonService implements IPersonService {

    private final PersonRepository personRepository;
    private final PositionRepository positionRepository;
    private final PersonStrategyManager strategyManager;
    private final ModelMapper modelMapper;

    public PersonService(PersonRepository personRepository, PositionRepository positionRepository, PersonStrategyManager strategyManager, ModelMapper modelMapper) {
        this.personRepository = personRepository;
        this.positionRepository = positionRepository;
        this.strategyManager = strategyManager;
        this.modelMapper = modelMapper;
    }

    @Override
    public <T extends PersonDto> T addPerson(T personDto) {
        @SuppressWarnings("unchecked")
        PersonTypeStrategy<? extends Person, T> strategy = (PersonTypeStrategy<? extends Person, T>) strategyManager.getStrategy(personDto.getType());

        if (strategy == null || !strategy.supports(personDto)) {
            throw new InvalidInputData("Nie znaleziono strategii dla podanego typu: " + personDto.getType());
        }
        Person person = strategy.addPerson(personDto);
        return modelMapper.map(person, (Class<T>) personDto.getClass());
    }

    @Override
    public <T extends PersonDto> T editPerson(Long id, T personDto) {
        T specificDto = modelMapper.map(personDto, (Class<T>) personDto.getClass());

        PersonTypeStrategy<? extends Person, ? extends PersonDto> strategy = strategyManager.getStrategy(specificDto.getType());

        if (strategy == null) {
            throw new InvalidInputData("Nie znaleziono strategii dla podanego typu: " + specificDto.getType());
        }
        if (!strategy.supports(specificDto)) {
            throw new InvalidInputData("Strategia nie obsługuje podanego typu DTO: " + specificDto.getClass().getName());
        }

        @SuppressWarnings("unchecked")
        PersonTypeStrategy<? extends Person, T> castedStrategy = (PersonTypeStrategy<? extends Person, T>) strategy;
        Person person = castedStrategy.editPerson(id, specificDto);
        return modelMapper.map(person, (Class<T>) personDto.getClass());
    }


    public Page<Person> searchPeople(Specification<Person> spec, Pageable pageable) {
        return personRepository.findAll(spec, pageable);
    }


    public EmployeeDto getEmployee(Long employeeId) {
        Employee employee = personRepository.findEmployeeById(employeeId)
                .orElseThrow(() -> new InvalidInputData("Nie znaleziono pracownika: " + employeeId));
        EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);
        return employeeDto;
    }
}

