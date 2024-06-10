package pl.kurs.finaltest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.kurs.finaltest.database.entity.Position;
import pl.kurs.finaltest.dto.PositionDto;
import pl.kurs.finaltest.dto.SimplePositionDto;
import pl.kurs.finaltest.services.impl.PositionService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestPropertySource(locations = "classpath:application-test.properties")
public class PositionControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private PositionService positionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void addPositionToEmployeeShouldReturnCreatedPosition() throws Exception {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();


        // Given
        SimplePositionDto simplePositionDto = new SimplePositionDto();
        simplePositionDto.setName("Senior Developer");
        simplePositionDto.setStartDate(LocalDate.of(2021, 1, 1));
        simplePositionDto.setEndDate(LocalDate.of(2022, 1, 1));
        simplePositionDto.setSalary(10000.0);

        Position position = new Position();
        position.setId(1L);
        position.setName("Senior Developer");
        position.setStartDate(LocalDate.of(2021, 1, 1));
        position.setEndDate(LocalDate.of(2022, 1, 1));
        position.setSalary(10000.0);

        given(positionService.addPosition(anyLong(), any(SimplePositionDto.class))).willReturn(position);

        // When & Then
        mockMvc.perform(post("/api/positions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(simplePositionDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Senior Developer"));
    }

    @Test
    public void updatePositionShouldReturnUpdatedPosition() throws Exception {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Given
        PositionDto positionDto = new PositionDto();
        positionDto.setId(1L);
        positionDto.setName("Lead Developer");
        positionDto.setStartDate(LocalDate.of(2021, 1, 1));
        positionDto.setEndDate(LocalDate.of(2022, 1, 1));
        positionDto.setSalary(12000.0);

        Position updatedPosition = new Position();
        updatedPosition.setId(1L);
        updatedPosition.setName("Lead Developer");
        updatedPosition.setStartDate(LocalDate.of(2021, 1, 1));
        updatedPosition.setEndDate(LocalDate.of(2022, 1, 1));
        updatedPosition.setSalary(12000.0);

        given(positionService.updatePosition(anyLong(), any(PositionDto.class))).willReturn(updatedPosition);

        // When & Then
        mockMvc.perform(put("/api/positions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(positionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Lead Developer"));
    }

    @Test
    public void listPositionsForEmployeeShouldReturnListOfPositions() throws Exception {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Given
        Position position1 = new Position();
        position1.setId(1L);
        position1.setName("Junior Developer");
        position1.setStartDate(LocalDate.of(2020, 1, 1));
        position1.setEndDate(LocalDate.of(2021, 1, 1));
        position1.setSalary(5000.0);

        Position position2 = new Position();
        position2.setId(2L);
        position2.setName("Senior Developer");
        position2.setStartDate(LocalDate.of(2021, 1, 1));
        position2.setEndDate(LocalDate.of(2022, 1, 1));
        position2.setSalary(10000.0);

        Set<Position> positions = new HashSet<>(Arrays.asList(position1, position2));

        given(positionService.findPositionsByEmployeeId(anyLong())).willReturn(positions);

        // When & Then
        mockMvc.perform(get("/api/positions/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Junior Developer"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Senior Developer"));
    }

}