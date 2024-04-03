package pl.kurs.finaltest.services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import pl.kurs.finaltest.criteria.PersonCriteria;
import pl.kurs.finaltest.dto.PersonDto;
import pl.kurs.finaltest.models.Employee;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.repositories.PersonRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PersonService personService;

    @Test
    void shouldAddPersonSuccessfully() {
        // Given
        PersonDto personDto = new PersonDto();
        Employee expectedEmployee = new Employee();

        when(modelMapper.map(any(PersonDto.class), eq(Employee.class))).thenReturn(expectedEmployee);
        when(personRepository.save(any(Employee.class))).thenReturn(expectedEmployee);

        // When
        Person result = personService.addPerson(personDto);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof Employee);
        verify(personRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void shouldEditPersonSuccessfully() {
        // Given
        Long personId = 1L;
        PersonDto personDto = new PersonDto();
        Employee existingEmployee = new Employee();
        Employee updatedEmployee = new Employee();

        when(personRepository.findById(personId)).thenReturn(Optional.of(existingEmployee));
        when(modelMapper.map(personDto, Employee.class)).thenReturn(updatedEmployee);
        when(personRepository.save(updatedEmployee)).thenReturn(updatedEmployee);
        when(modelMapper.map(updatedEmployee, PersonDto.class)).thenReturn(personDto);

        // When
        Person result = personService.editPerson(personId, personDto);

        // Then
        assertNotNull(result);
        assertEquals(updatedEmployee, result);
        verify(personRepository, times(1)).save(updatedEmployee);
    }

    @Test
    void shouldSearchPersonsSuccessfully() {
        // Given
        PersonCriteria searchCriteria = new PersonCriteria();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Employee> expectedPage = new PageImpl<>(List.of(new Employee()));

        when(personRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        // When
        Page<Person> resultPage = personService.findPersons(searchCriteria, pageable);

        // Then
        assertNotNull(resultPage);
        assertEquals(expectedPage.getSize(), resultPage.getSize());
        verify(personRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void shouldThrowWhenPersonNotFound() {
        // Given
        Long personId = 1L;
        PersonDto personDto = new PersonDto();

        when(personRepository.findById(personId)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(EntityNotFoundException.class, () -> personService.editPerson(personId, personDto));

        // Then
        assertTrue(exception.getMessage().contains("Nie znaleziono osoby"));
    }

}