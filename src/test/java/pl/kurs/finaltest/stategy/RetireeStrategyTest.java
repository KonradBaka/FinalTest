package pl.kurs.finaltest.stategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.finaltest.database.entity.Person;
import pl.kurs.finaltest.database.entity.Retiree;
import pl.kurs.finaltest.database.repositories.PersonRepository;
import pl.kurs.finaltest.dto.RetireeDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class RetireeStrategyTest {

    @Autowired
    private RetireeStrategy retireeStrategy;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        personRepository.deleteAll();
    }

    @Test
    @Transactional
    void addPersonShouldSaveRetireeToDatabase() {
        // Given
        RetireeDto retireeDto = new RetireeDto();
        retireeDto.setFirstName("Alice");
        retireeDto.setLastName("Brown");
        retireeDto.setPesel("98765432101");
        retireeDto.setType("retiree");
        retireeDto.setPensionAmount(3000.0);
        retireeDto.setYearsWorked(40);

        // When
        Retiree savedRetiree = retireeStrategy.addPerson(retireeDto);

        // Then
        assertNotNull(savedRetiree.getId(), "Retiree ID should not be null");
        Optional<Person> foundRetiree = personRepository.findById(savedRetiree.getId());
        assertTrue(foundRetiree.isPresent(), "Retiree should be found in the database");
        assertEquals("Alice", foundRetiree.get().getFirstName(), "First name should match");
        assertEquals("Brown", foundRetiree.get().getLastName(), "Last name should match");
    }

    @Test
    @Transactional
    void editPersonShouldUpdateRetireeInDatabase() {
        // Given
        Retiree retiree = new Retiree();
        retiree.setFirstName("Alice");
        retiree.setLastName("Brown");
        retiree.setPesel("98765432101");
        retiree.setType("retiree");
        retiree.setPensionAmount(3000.0);
        retiree.setYearsWorked(40);
        retiree = personRepository.save(retiree);

        RetireeDto retireeDto = new RetireeDto();
        retireeDto.setFirstName("Eve");
        retireeDto.setLastName("Smith");
        retireeDto.setPesel("98765432101");
        retireeDto.setType("retiree");
        retireeDto.setPensionAmount(3500.0);
        retireeDto.setYearsWorked(45);

        // When
        Retiree updatedRetiree = retireeStrategy.editPerson(retiree.getId(), retireeDto);

        // Then
        assertNotNull(updatedRetiree, "Updated retiree should not be null");
        assertEquals(retiree.getId(), updatedRetiree.getId(), "Retiree ID should match");
        assertEquals("Eve", updatedRetiree.getFirstName(), "First name should be updated");
        assertEquals("Smith", updatedRetiree.getLastName(), "Last name should match");
        assertEquals(3500.0, updatedRetiree.getPensionAmount(), "Pension amount should be updated");
        assertEquals(45, updatedRetiree.getYearsWorked(), "Years worked should be updated");
    }

}