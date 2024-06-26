package pl.kurs.finaltest.stategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.finaltest.database.entity.Employee;
import pl.kurs.finaltest.database.entity.Person;
import pl.kurs.finaltest.database.repositories.PersonRepository;
import pl.kurs.finaltest.database.repositories.PositionRepository;
import pl.kurs.finaltest.dto.EmployeeDto;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class EmployeeStrategyTest {


    @Autowired
    private EmployeeStrategy employeeStrategy;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        personRepository.deleteAll();
        positionRepository.deleteAll();
    }

    @Test
    @Transactional
    void addPersonShouldSaveEmployeeToDatabase() {
        // Given
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("John");
        employeeDto.setLastName("Doe");
        employeeDto.setPesel("12345678901");
        employeeDto.setType("employee");
        employeeDto.setEmploymentStartDate(LocalDate.of(2020, 1, 15));
        employeeDto.setCurrentPosition("Software Engineer");
        employeeDto.setCurrentSalary(7500.0);

        // When
        Employee savedEmployee = employeeStrategy.addPerson(employeeDto);

        // Then
        assertNotNull(savedEmployee.getId(), "Employee ID should not be null");
        Optional<Person> foundEmployee = personRepository.findById(savedEmployee.getId());
        assertTrue(foundEmployee.isPresent(), "Employee should be found in the database");
        assertEquals("John", foundEmployee.get().getFirstName(), "First name should match");
        assertEquals("Doe", foundEmployee.get().getLastName(), "Last name should match");
    }

    @Test
    @Transactional
    void editPersonShouldUpdateEmployeeInDatabase() {
        // Given
        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setPesel("12345678901");
        employee.setType("employee");
        employee.setEmploymentStartDate(LocalDate.of(2020, 1, 15));
        employee.setCurrentPosition("Software Engineer");
        employee.setCurrentSalary(7500.0);
        employee = personRepository.save(employee);

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Jane");
        employeeDto.setLastName("Doe");
        employeeDto.setPesel("12345678901");
        employeeDto.setType("employee");
        employeeDto.setEmploymentStartDate(LocalDate.of(2021, 1, 15));
        employeeDto.setCurrentPosition("Senior Software Engineer");
        employeeDto.setCurrentSalary(9500.0);

        // When
        Employee updatedEmployee = employeeStrategy.editPerson(employee.getId(), employeeDto);

        // Then
        assertNotNull(updatedEmployee, "Updated employee should not be null");
        assertEquals(employee.getId(), updatedEmployee.getId(), "Employee ID should match");
        assertEquals("Jane", updatedEmployee.getFirstName(), "First name should be updated");
        assertEquals("Doe", updatedEmployee.getLastName(), "Last name should match");
        assertEquals(LocalDate.of(2021, 1, 15), updatedEmployee.getEmploymentStartDate(), "Employment start date should be updated");
        assertEquals("Senior Software Engineer", updatedEmployee.getCurrentPosition(), "Current position should be updated");
        assertEquals(9500.0, updatedEmployee.getCurrentSalary(), "Current salary should be updated");
    }

}