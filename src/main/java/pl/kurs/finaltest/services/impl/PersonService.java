package pl.kurs.finaltest.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.finaltest.criteria.PersonCriteria;
import pl.kurs.finaltest.criteria.StudentCriteria;
import pl.kurs.finaltest.database.entity.Employee;
import pl.kurs.finaltest.database.repositories.PositionRepository;
import pl.kurs.finaltest.dto.DtoManager;
import pl.kurs.finaltest.dto.EmployeeDto;
import pl.kurs.finaltest.dto.PersonDto;
import pl.kurs.finaltest.database.specification.SpecificationManager;
import pl.kurs.finaltest.exceptions.InvalidInputData;
import pl.kurs.finaltest.database.entity.Person;
import pl.kurs.finaltest.database.repositories.PersonRepository;
import pl.kurs.finaltest.exceptions.SessionNotFoundException;
import pl.kurs.finaltest.services.IPersonService;
import pl.kurs.finaltest.services.PersonTypeStrategy;

@Service
@Transactional
public class PersonService implements IPersonService {

    private final PersonRepository personRepository;
    private final PositionRepository positionRepository;
    private final PersonStrategyManager strategyManager;
    private final SpecificationManager specificationManager;
    private final DtoManager dtoManager;
    private final ModelMapper modelMapper;

    public PersonService(PersonRepository personRepository, PositionRepository positionRepository, PersonStrategyManager strategyManager, SpecificationManager specificationManager, DtoManager dtoManager, ModelMapper modelMapper) {
        this.personRepository = personRepository;
        this.positionRepository = positionRepository;
        this.strategyManager = strategyManager;
        this.specificationManager = specificationManager;
        this.dtoManager = dtoManager;
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
        PersonTypeStrategy<? extends Person, ? extends PersonDto> strategy = strategyManager.getStrategy(personDto.getType());

        if (strategy == null) {
            throw new InvalidInputData("Nie znaleziono strategii dla podanego typu: " + personDto.getType());
        }

        if (!strategy.supports(personDto)) {
            throw new InvalidInputData("Strategia nie obsługuje podanego typu DTO: " + personDto.getClass().getName());
        }

        @SuppressWarnings("unchecked")
        PersonTypeStrategy<? extends Person, T> castedStrategy = (PersonTypeStrategy<? extends Person, T>) strategy;
        Person person = castedStrategy.editPerson(id, personDto);
        return modelMapper.map(person, (Class<T>) personDto.getClass());
    }



    public Page<PersonDto> findPersons(PersonCriteria criteria, Pageable pageable) {
        Specification<Person> spec = specificationManager.getSpecification(criteria);
        Page<Person> persons = personRepository.findAll(spec, pageable);
        return persons.map(this::convertToDto);
    }

    private PersonDto convertToDto(Person person) {
        Class<? extends PersonDto> dtoClass = dtoManager.getDtoClass(person.getClass());
        return modelMapper.map(person, dtoClass);
    }



    public EmployeeDto getEmployee(Long employeeId) {
        Employee employee = personRepository.findEmployeeById(employeeId)
                .orElseThrow(() -> new SessionNotFoundException("Nie znaleziono pracownika: " + employeeId));
        EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);
        updateEmployeeDtoPositions(employee, employeeDto);
        return employeeDto;
    }

    protected void updateEmployeeDtoPositions(Employee employee, EmployeeDto employeeDto) {
        Long count = positionRepository.countByEmployeeId(employee.getId());
        employeeDto.setNumberOfPositions(count.intValue());
    }

    protected void updatePositionsForEmployee(Employee employee) {
        long positionCount = positionRepository.countByEmployeeId(employee.getId());
        EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);
        employeeDto.setNumberOfPositions((int) positionCount);
    }
}

