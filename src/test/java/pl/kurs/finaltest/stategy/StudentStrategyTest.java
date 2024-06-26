package pl.kurs.finaltest.stategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.finaltest.database.entity.Person;
import pl.kurs.finaltest.database.entity.Student;
import pl.kurs.finaltest.database.repositories.PersonRepository;
import pl.kurs.finaltest.dto.StudentDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class StudentStrategyTest {


    @Autowired
    private StudentStrategy studentStrategy;

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
    void addPersonShouldSaveStudentToDatabase() {
        // Given
        StudentDto studentDto = new StudentDto();
        studentDto.setFirstName("Tom");
        studentDto.setLastName("Hardy");
        studentDto.setPesel("12345678904");
        studentDto.setType("student");
        studentDto.setUniversityName("Harvard");
        studentDto.setYearOfStudy(2);
        studentDto.setFieldOfStudy("Computer Science");
        studentDto.setScholarshipAmount(2000.0);

        // When
        Student savedStudent = studentStrategy.addPerson(studentDto);

        // Then
        assertNotNull(savedStudent.getId(), "Student ID should not be null");
        Optional<Person> foundStudent = personRepository.findById(savedStudent.getId());
        assertTrue(foundStudent.isPresent(), "Student should be found in the database");
        assertEquals("Tom", foundStudent.get().getFirstName(), "First name should match");
        assertEquals("Hardy", foundStudent.get().getLastName(), "Last name should match");
    }

    @Test
    @Transactional
    void editPersonShouldUpdateStudentInDatabase() {
        // Given
        Student student = new Student();
        student.setFirstName("Tom");
        student.setLastName("Hardy");
        student.setPesel("12345678904");
        student.setType("student");
        student.setUniversityName("Harvard");
        student.setYearOfStudy(2);
        student.setFieldOfStudy("Computer Science");
        student.setScholarshipAmount(2000.0);
        student = personRepository.save(student);

        StudentDto studentDto = new StudentDto();
        studentDto.setFirstName("John");
        studentDto.setLastName("Doe");
        studentDto.setPesel("12345678904");
        studentDto.setType("student");
        studentDto.setUniversityName("MIT");
        studentDto.setYearOfStudy(3);
        studentDto.setFieldOfStudy("Physics");
        studentDto.setScholarshipAmount(2500.0);

        // When
        Student updatedStudent = studentStrategy.editPerson(student.getId(), studentDto);

        // Then
        assertNotNull(updatedStudent, "Updated student should not be null");
        assertEquals(student.getId(), updatedStudent.getId(), "Student ID should match");
        assertEquals("John", updatedStudent.getFirstName(), "First name should be updated");
        assertEquals("Doe", updatedStudent.getLastName(), "Last name should match");
        assertEquals("MIT", updatedStudent.getUniversityName(), "University name should be updated");
        assertEquals(3, updatedStudent.getYearOfStudy(), "Year of study should be updated");
        assertEquals("Physics", updatedStudent.getFieldOfStudy(), "Field of study should be updated");
        assertEquals(2500.0, updatedStudent.getScholarshipAmount(), "Scholarship amount should be updated");
    }
}