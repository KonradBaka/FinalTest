package pl.kurs.finaltest.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.kurs.finaltest.database.entity.Person;
import pl.kurs.finaltest.dto.*;
import pl.kurs.finaltest.services.impl.PersonService;
import pl.kurs.finaltest.services.impl.SpecificationService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestPropertySource(locations = "classpath:application-test.properties")
public class PersonControllerTest {


    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @MockBean
    private SpecificationService specificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void searchPeopleShouldReturnPagedResults() throws Exception {
        // Given
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        List<Person> peopleList = List.of(new Person() {
        });
        Page<Person> peoplePage = new PageImpl<>(peopleList);
        given(personService.searchPeople(any(), any()))
                .willReturn(peoplePage);

        // When & Then
        mockMvc.perform(get("/api/people")
                        .param("criteriaKey", "criteriaValue")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(peoplePage)));
    }

    @Test
    public void addPersonShouldReturnCreatedRetiree() throws Exception {
        // Given
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        RetireeDto retireeDto = new RetireeDto();
        retireeDto.setFirstName("John");
        retireeDto.setLastName("Doe");
        retireeDto.setPesel("12345678901");
        retireeDto.setType("retiree");
        retireeDto.setPensionAmount(2000.50);
        retireeDto.setYearsWorked(35);

        // Mock
        given(personService.addPerson(any(PersonDto.class))).willReturn(retireeDto);

        // When & Then
        mockMvc.perform(post("/api/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(retireeDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(retireeDto)));
    }

    @Test
    public void addPersonShouldReturnCreatedStudent() throws Exception {
        // Given
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        StudentDto studentDto = new StudentDto();
        studentDto.setFirstName("Tom");
        studentDto.setLastName("Nowak");
        studentDto.setPesel("12377678901");
        studentDto.setType("student");
        studentDto.setUniversityName("Cambridge");
        studentDto.setFieldOfStudy("Architecture");
        studentDto.setScholarshipAmount(100.50);
        studentDto.setYearOfStudy(4);

        // Mock
        given(personService.addPerson(any(PersonDto.class))).willReturn(studentDto);

        // When & Then
        mockMvc.perform(post("/api/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(studentDto)));
    }

    @Test
    public void addPersonShouldReturnCreatedEmployee() throws Exception {
        // Given
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Alice");
        employeeDto.setLastName("Johnson");
        employeeDto.setPesel("12345678903");
        employeeDto.setType("employee");
        employeeDto.setHeight(170.0);
        employeeDto.setWeight(65.0);
        employeeDto.setEmailAddress("alice.johnson@example.com");
        employeeDto.setEmploymentStartDate(LocalDate.of(2020, 1, 15));
        employeeDto.setCurrentPosition("Software Engineer");
        employeeDto.setCurrentSalary(7500.0);
        SimplePositionDto positionDto = new SimplePositionDto();
        positionDto.setName("Junior Developer");
        positionDto.setStartDate(LocalDate.of(2018, 6, 1));
        positionDto.setEndDate(LocalDate.of(2020, 1, 14));
        positionDto.setSalary(5000.0);
        employeeDto.setPositions(Set.of(positionDto));

        // Mock
        given(personService.addPerson(any(PersonDto.class))).willReturn(employeeDto);

        // When & Then
        mockMvc.perform(post("/api/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(employeeDto)));
    }

    @Test
    public void editPersonShouldReturnUpdatedPerson() throws Exception {
        // Given
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        RetireeDto retireeDto = new RetireeDto();
        retireeDto.setFirstName("John");
        retireeDto.setLastName("Doe");
        retireeDto.setPesel("12345678901");
        retireeDto.setType("retiree");
        retireeDto.setPensionAmount(2000.50);
        retireeDto.setYearsWorked(35);

        // Mock the service
        given(personService.editPerson(anyLong(), any(PersonDto.class))).willReturn(retireeDto);

        // When & Then
        mockMvc.perform(put("/api/people/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(retireeDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(retireeDto)));
    }
}