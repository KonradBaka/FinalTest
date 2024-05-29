package pl.kurs.finaltest.services.impl;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.TestPropertySource;
import pl.kurs.finaltest.database.entity.Employee;
import pl.kurs.finaltest.database.entity.Person;
import pl.kurs.finaltest.database.entity.Retiree;
import pl.kurs.finaltest.database.repositories.PersonRepository;
import pl.kurs.finaltest.database.repositories.PositionRepository;
import pl.kurs.finaltest.dto.EmployeeDto;
import pl.kurs.finaltest.dto.RetireeDto;
import pl.kurs.finaltest.exceptions.InvalidInputData;
import pl.kurs.finaltest.stategy.PersonStrategyManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class PersonServiceTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private PersonStrategyManager strategyManager;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PersonService personService;

    @BeforeEach
    void setUp() {
        personRepository.deleteAll();
        positionRepository.deleteAll();
    }

    @Test
    @Transactional
    void addPersonShouldAddPersonToDatabase() {
        // Given
        RetireeDto retireeDto = new RetireeDto();
        retireeDto.setFirstName("John");
        retireeDto.setLastName("Doe");
        retireeDto.setPesel("32145678901");
        retireeDto.setType("retiree");
        retireeDto.setPensionAmount(2000.50);
        retireeDto.setYearsWorked(35);

        // When
        RetireeDto savedRetireeDto = personService.addPerson(retireeDto);

        // Then
        assertNotNull(savedRetireeDto, "Saved RetireeDto should not be null");
        assertNotNull(savedRetireeDto.getId(), "Saved RetireeDto ID should not be null");

        Optional<Retiree> savedRetiree = personRepository.findPersonById(savedRetireeDto.getId());
        assertTrue(savedRetiree.isPresent(), "Retiree should be present in the database");
        assertEquals("John", savedRetiree.get().getFirstName(), "First name should match");
        assertEquals("Doe", savedRetiree.get().getLastName(), "Last name should match");
    }


    @Test
    @Transactional
    void searchPeopleShouldReturnPagedResults() {
        // Given
        Retiree retiree1 = new Retiree();
        retiree1.setFirstName("Tom");
        retiree1.setLastName("Roney");

        retiree1.setPesel("66545678902");
        retiree1.setPensionAmount(2000.50);
        retiree1.setYearsWorked(35);
        personRepository.save(retiree1);

        Retiree retiree2 = new Retiree();
        retiree2.setFirstName("Jane");
        retiree2.setLastName("Doe");
        retiree2.setPesel("32145678903");
        retiree2.setPensionAmount(2500.75);
        retiree2.setYearsWorked(40);
        personRepository.save(retiree2);

        Specification<Person> spec = mock(Specification.class);

        // When
        Page<Person> result = personService.searchPeople(spec, PageRequest.of(0, 10));

        // Then
        assertEquals(2, result.getTotalElements(), "Total elements should be 2");
        assertEquals(2, result.getContent().size(), "Page content size should be 2");
    }

    @Test
    @Transactional
    void getEmployeeShouldReturnEmployeeDto() {
        // Given
        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setPesel("12345678904");
        personRepository.save(employee);

        // When
        EmployeeDto result = personService.getEmployee(employee.getId());

        // Then
        assertNotNull(result, "EmployeeDto should not be null");
        assertEquals(employee.getId(), result.getId(), "ID should match");
        assertEquals("John", result.getFirstName(), "First name should match");
        assertEquals("Doe", result.getLastName(), "Last name should match");
    }

    @Test
    @Transactional
    void getEmployeeShouldThrowExceptionWhenNotFound() {
        // Given
        Long invalidId = 999L;

        // When & Then
        assertThrows(InvalidInputData.class, () -> personService.getEmployee(invalidId), "Should throw InvalidInputData exception");
    }

    @Test
    @Transactional
    void getRetireeShouldThrowExceptionWhenNotFound() {
        // Given
        Long invalidId = 999L;

        // When & Then
        assertThrows(InvalidInputData.class, () -> personService.getEmployee(invalidId), "Should throw InvalidInputData exception");
    }

}

