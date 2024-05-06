package pl.kurs.finaltest.controllers;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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