package pl.kurs.finaltest.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import pl.kurs.finaltest.criteria.PersonCriteria;
import pl.kurs.finaltest.dto.PersonDto;
import pl.kurs.finaltest.models.Employee;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.services.PersonService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class PersonControllerTest {

//    @Mock
//    private PersonService personService;
//
//    @Mock
//    private ModelMapper modelMapper;
//
//    @InjectMocks
//    private PersonController personController;
//
//    @Test
//    void shouldAddPersonSuccessfully() {
//        // Given
//        PersonDto personDtoInput = new PersonDto();
//        PersonDto personDtoOutput = new PersonDto();
//        personDtoOutput.setFirstName("John");
//        personDtoOutput.setLastName("Doe");
//
//        Employee employee = new Employee();
//        employee.setFirstName("John");
//        employee.setLastName("Doe");
//
//        when(modelMapper.map(any(PersonDto.class), eq(Employee.class))).thenReturn(employee);
//        when(personService.addPerson(any(PersonDto.class))).thenReturn(employee);
//        when(modelMapper.map(any(Employee.class), eq(PersonDto.class))).thenReturn(personDtoOutput);
//
//        // When
//        ResponseEntity<PersonDto> response = personController.addPerson(personDtoInput);
//
//        // Then
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals("John", response.getBody().getFirstName());
//        assertEquals("Doe", response.getBody().getLastName());
//
//        verify(personService, times(1)).addPerson(any(PersonDto.class));
//        verify(modelMapper, times(1)).map(any(PersonDto.class), eq(Employee.class));
//        verify(modelMapper, times(1)).map(any(Employee.class), eq(PersonDto.class));
//    }

//    @Test
//    void shouldSearchPersonsSuccessfully() {
//        // Given
//        PersonCriteria searchCriteria = new PersonCriteria();
//        Pageable pageable = PageRequest.of(0, 10);
//
//        Employee employee = new Employee();
//        Page<Person> page = new PageImpl<>(List.of(employee));
//
//        PersonDto personDto = new PersonDto();
//        personDto.setFirstName("John");
//        personDto.setLastName("Doe");
//
//        when(personService.findPersons(searchCriteria, pageable)).thenReturn(page);
//        when(modelMapper.map(any(Employee.class), eq(PersonDto.class))).thenReturn(personDto);
//
//        // When
//        ResponseEntity<Page<PersonDto>> response = personController.searchPersons(searchCriteria, pageable);
//
//        // Then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals(1, response.getBody().size());
//        PersonDto resultDto = response.getBody().get(0);
//        assertEquals("John", resultDto.getFirstName());
//        assertEquals("Doe", resultDto.getLastName());
//
//        verify(personService, times(1)).findPersons(searchCriteria, pageable);
//        verify(modelMapper, times(1)).map(any(Employee.class), eq(PersonDto.class));
//    }
}