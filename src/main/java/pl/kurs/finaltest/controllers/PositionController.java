package pl.kurs.finaltest.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.finaltest.dto.PositionDto;
import pl.kurs.finaltest.models.Position;
import pl.kurs.finaltest.services.PositionService;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/positions")
public class PositionController {

    private PositionService positionService;
    private ModelMapper modelMapper;

    public PositionController(PositionService positionService, ModelMapper modelMapper) {
        this.positionService = positionService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/employee/{employeeId}")
    public ResponseEntity<PositionDto> addPositionToEmployee(@PathVariable Long employeeId, @RequestBody PositionDto positionDto) {
        Position position = positionService.addPosition(employeeId, positionDto);
        PositionDto createdPositionDto = new ModelMapper().map(position, PositionDto.class);
        return new ResponseEntity<>(createdPositionDto, HttpStatus.CREATED);
    }

    @PutMapping("/{positionId}")
    public ResponseEntity<PositionDto> updatePosition(@PathVariable Long positionId, @RequestBody PositionDto positionDto) {
        Position updatedPosition = positionService.updatePosition(positionId, positionDto);
        PositionDto updatedPositionDto = modelMapper.map(updatedPosition, PositionDto.class);
        return ResponseEntity.ok(updatedPositionDto);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Set<PositionDto>> listPositionsForEmployee(@PathVariable Long employeeId) {
        Set<Position> positions = positionService.findPositionsByEmployeeId(employeeId);
        Set<PositionDto> positionDtos = positions.stream()
                .map(position -> modelMapper.map(position, PositionDto.class))
                .collect(Collectors.toSet());
        return ResponseEntity.ok(positionDtos);
    }

}
