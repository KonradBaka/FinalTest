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
import pl.kurs.finaltest.database.entity.Student;
import pl.kurs.finaltest.database.repositories.PersonRepository;
import pl.kurs.finaltest.database.repositories.PositionRepository;
import pl.kurs.finaltest.dto.EmployeeDto;
import pl.kurs.finaltest.dto.RetireeDto;
import pl.kurs.finaltest.dto.StudentDto;
import pl.kurs.finaltest.exceptions.InvalidInputData;
import pl.kurs.finaltest.stategy.PersonStrategyManager;

import java.time.LocalDate;
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
    void addPersonShouldAddRetireeToDatabase() {
        // Given
        RetireeDto retireeDto = new RetireeDto();
        retireeDto.setFirstName("John");
        retireeDto.setLastName("Doe");
        retireeDto.setPesel("12345678901");
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
    void addPersonShouldAddStudentToDatabase() {
        // Given
        StudentDto studentDto = new StudentDto();
        studentDto.setFirstName("Alice");
        studentDto.setLastName("Smith");
        studentDto.setPesel("12345678902");
        studentDto.setType("student");
        studentDto.setUniversityName("MIT");
        studentDto.setYearOfStudy(2);
        studentDto.setFieldOfStudy("Computer Science");
        studentDto.setScholarshipAmount(1500.75);

        // When
        StudentDto savedStudentDto = personService.addPerson(studentDto);

        // Then
        assertNotNull(savedStudentDto, "Saved StudentDto should not be null");
        assertNotNull(savedStudentDto.getId(), "Saved StudentDto ID should not be null");

        Optional<Student> savedStudent = personRepository.findPersonById(savedStudentDto.getId());
        assertTrue(savedStudent.isPresent(), "Student should be present in the database");
        assertEquals("Alice", savedStudent.get().getFirstName(), "First name should match");
        assertEquals("Smith", savedStudent.get().getLastName(), "Last name should match");
    }

    @Test
    @Transactional
    void addPersonShouldAddEmployeeToDatabase() {
        // Given
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Bob");
        employeeDto.setLastName("Johnson");
        employeeDto.setPesel("12345678903");
        employeeDto.setType("employee");
        employeeDto.setEmploymentStartDate(LocalDate.of(2021, 1, 1));
        employeeDto.setCurrentPosition("Software Engineer");
        employeeDto.setCurrentSalary(7500.00);

        // When
        EmployeeDto savedEmployeeDto = personService.addPerson(employeeDto);

        // Then
        assertNotNull(savedEmployeeDto, "Saved EmployeeDto should not be null");
        assertNotNull(savedEmployeeDto.getId(), "Saved EmployeeDto ID should not be null");

        Optional<Employee> savedEmployee = personRepository.findPersonById(savedEmployeeDto.getId());
        assertTrue(savedEmployee.isPresent(), "Employee should be present in the database");
        assertEquals("Bob", savedEmployee.get().getFirstName(), "First name should match");
        assertEquals("Johnson", savedEmployee.get().getLastName(), "Last name should match");
    }

    @Test
    @Transactional
    void editPersonShouldUpdateRetireeInDatabase() {
        // Given
        Retiree retiree = new Retiree();
        retiree.setFirstName("John");
        retiree.setLastName("Doe");
        retiree.setPesel("12345678901");
        retiree.setPensionAmount(2000.50);
        retiree.setYearsWorked(35);
        personRepository.save(retiree);

        RetireeDto retireeDto = new RetireeDto();
        retireeDto.setFirstName("Jane");
        retireeDto.setLastName("Doe");
        retireeDto.setPesel("12345678901");
        retireeDto.setType("retiree");
        retireeDto.setPensionAmount(3500.75);
        retireeDto.setYearsWorked(40);

        // When
        RetireeDto updatedRetireeDto = personService.editPerson(retiree.getId(), retireeDto);

        // Then
        assertNotNull(updatedRetireeDto, "Updated RetireeDto should not be null");
        assertEquals(retiree.getId(), updatedRetireeDto.getId(), "Updated RetireeDto ID should match");

        Optional<Retiree> updatedRetiree = personRepository.findPersonById(updatedRetireeDto.getId());
        assertTrue(updatedRetiree.isPresent(), "Retiree should be present in the database");
        assertEquals("Jane", updatedRetiree.get().getFirstName(), "First name should be updated");
        assertEquals("Doe", updatedRetiree.get().getLastName(), "Last name should match");
        assertEquals(3500.75, updatedRetiree.get().getPensionAmount(), "Pension amount should be updated");
        assertEquals(40, updatedRetiree.get().getYearsWorked(), "Years worked should be updated");
    }

    @Test
    @Transactional
    void editPersonShouldUpdateStudentInDatabase() {
        // Given
        Student student = new Student();
        student.setFirstName("Alice");
        student.setLastName("Smith");
        student.setPesel("12345678902");
        student.setUniversityName("MIT");
        student.setYearOfStudy(2);
        student.setFieldOfStudy("Computer Science");
        student.setScholarshipAmount(1500.75);
        personRepository.save(student);

        StudentDto studentDto = new StudentDto();
        studentDto.setFirstName("Alice");
        studentDto.setLastName("Johnson");
        studentDto.setPesel("12345678902");
        studentDto.setType("student");
        studentDto.setUniversityName("Harvard");
        studentDto.setYearOfStudy(3);
        studentDto.setFieldOfStudy("Law");
        studentDto.setScholarshipAmount(2000.00);

        // When
        StudentDto updatedStudentDto = personService.editPerson(student.getId(), studentDto);

        // Then
        assertNotNull(updatedStudentDto, "Updated StudentDto should not be null");
        assertEquals(student.getId(), updatedStudentDto.getId(), "Updated StudentDto ID should match");

        Optional<Student> updatedStudent = personRepository.findPersonById(updatedStudentDto.getId());
        assertTrue(updatedStudent.isPresent(), "Student should be present in the database");
        assertEquals("Alice", updatedStudent.get().getFirstName(), "First name should match");
        assertEquals("Johnson", updatedStudent.get().getLastName(), "Last name should be updated");
        assertEquals("Harvard", updatedStudent.get().getUniversityName(), "University name should be updated");
        assertEquals(3, updatedStudent.get().getYearOfStudy(), "Year of study should be updated");
        assertEquals("Law", updatedStudent.get().getFieldOfStudy(), "Field of study should be updated");
        assertEquals(2000.00, updatedStudent.get().getScholarshipAmount(), "Scholarship amount should be updated");
    }

    @Test
    @Transactional
    void editPersonShouldUpdateEmployeeInDatabase() {
        // Given
        Employee employee = new Employee();
        employee.setFirstName("Bob");
        employee.setLastName("Johnson");
        employee.setPesel("12345678903");
        employee.setCurrentPosition("Software Engineer");
        employee.setCurrentSalary(7500.00);
        employee.setEmploymentStartDate(LocalDate.of(2021, 1, 1));
        personRepository.save(employee);

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Robert");
        employeeDto.setLastName("Johnson");
        employeeDto.setPesel("12345678903");
        employeeDto.setType("employee");
        employeeDto.setEmploymentStartDate(LocalDate.of(2021, 1, 1));
        employeeDto.setCurrentPosition("Senior Software Engineer");
        employeeDto.setCurrentSalary(9000.00);

        // When
        EmployeeDto updatedEmployeeDto = personService.editPerson(employee.getId(), employeeDto);

        // Then
        assertNotNull(updatedEmployeeDto, "Updated EmployeeDto should not be null");
        assertEquals(employee.getId(), updatedEmployeeDto.getId(), "Updated EmployeeDto ID should match");

        Optional<Employee> updatedEmployee = personRepository.findPersonById(updatedEmployeeDto.getId());
        assertTrue(updatedEmployee.isPresent(), "Employee should be present in the database");
        assertEquals("Robert", updatedEmployee.get().getFirstName(), "First name should be updated");
        assertEquals("Johnson", updatedEmployee.get().getLastName(), "Last name should match");
        assertEquals("Senior Software Engineer", updatedEmployee.get().getCurrentPosition(), "Position should be updated");
        assertEquals(9000.00, updatedEmployee.get().getCurrentSalary(), "Salary should be updated");
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

