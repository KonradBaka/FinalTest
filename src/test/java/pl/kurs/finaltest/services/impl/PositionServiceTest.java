package pl.kurs.finaltest.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.finaltest.database.entity.Employee;
import pl.kurs.finaltest.database.entity.Position;
import pl.kurs.finaltest.database.repositories.PersonRepository;
import pl.kurs.finaltest.database.repositories.PositionRepository;
import pl.kurs.finaltest.dto.PositionDto;
import pl.kurs.finaltest.dto.SimplePositionDto;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class PositionServiceTest {

    @Autowired
    private PositionService positionService;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    @BeforeEach
    void setUp() {
        positionRepository.deleteAll();
        personRepository.deleteAll();
    }

    @Test
    @Transactional
    void addPositionShouldAddPositionToEmployee() {
        // Given
        Employee employee = createAndSaveEmployee("John", "Doe", "12345678905");
        SimplePositionDto positionDto = new SimplePositionDto();
        positionDto.setName("Developer");
        positionDto.setStartDate(LocalDate.of(2023, 1, 1));
        positionDto.setEndDate(LocalDate.of(2023, 12, 31));
        positionDto.setSalary(5000.0);

        // When
        Position savedPosition = positionService.addPosition(employee.getId(), positionDto);

        // Then
        assertNotNull(savedPosition, "Saved position should not be null");
        assertNotNull(savedPosition.getId(), "Saved position ID should not be null");
        assertEquals(employee.getId(), savedPosition.getEmployee().getId(), "Employee ID should match");
        assertEquals("Developer", savedPosition.getName(), "Position name should match");
        assertEquals(LocalDate.of(2023, 1, 1), savedPosition.getStartDate(), "Start date should match");
        assertEquals(LocalDate.of(2023, 12, 31), savedPosition.getEndDate(), "End date should match");
        assertEquals(5000.0, savedPosition.getSalary(), "Salary should match");
    }

    @Test
    @Transactional
    void updatePositionShouldUpdateExistingPosition() {
        // Given
        Employee employee = createAndSaveEmployee("Jane", "Doe", "12345678906");
        Position position = createAndSavePosition(employee, "Developer", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31), 5000.0);
        PositionDto positionDto = new PositionDto();
        positionDto.setName("Senior Developer");
        positionDto.setStartDate(LocalDate.of(2023, 1, 1));
        positionDto.setEndDate(LocalDate.of(2023, 12, 31));
        positionDto.setSalary(7000.0);

        // When
        Position updatedPosition = positionService.updatePosition(position.getId(), positionDto);

        // Then
        assertNotNull(updatedPosition, "Updated position should not be null");
        assertEquals(position.getId(), updatedPosition.getId(), "Position ID should match");
        assertEquals(employee.getId(), updatedPosition.getEmployee().getId(), "Employee ID should match");
        assertEquals("Senior Developer", updatedPosition.getName(), "Position name should be updated");
        assertEquals(LocalDate.of(2023, 1, 1), updatedPosition.getStartDate(), "Start date should match");
        assertEquals(LocalDate.of(2023, 12, 31), updatedPosition.getEndDate(), "End date should match");
        assertEquals(7000.0, updatedPosition.getSalary(), "Salary should be updated");
    }

    @Test
    @Transactional
    void findPositionsByEmployeeIdShouldReturnAllPositions() {
        // Given
        Employee employee = createAndSaveEmployee("Mike", "Johnson", "12345678907");
        createAndSavePosition(employee, "Developer", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31), 5000.0);
        createAndSavePosition(employee, "Manager", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), 7000.0);

        // When
        Set<Position> positions = positionService.findPositionsByEmployeeId(employee.getId());

        // Then
        assertNotNull(positions, "Positions should not be null");
        assertEquals(2, positions.size(), "There should be 2 positions");
    }

    private Employee createAndSaveEmployee(String firstName, String lastName, String pesel) {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setPesel(pesel);
        employee.setEmploymentStartDate(LocalDate.now());
        employee.setCurrentPosition("Developer");
        employee.setCurrentSalary(5000.0);
        return personRepository.save(employee);
    }

    private Position createAndSavePosition(Employee employee, String name, LocalDate startDate, LocalDate endDate, Double salary) {
        Position position = new Position();
        position.setEmployee(employee);
        position.setName(name);
        position.setStartDate(startDate);
        position.setEndDate(endDate);
        position.setSalary(salary);
        return positionRepository.save(position);
    }
}