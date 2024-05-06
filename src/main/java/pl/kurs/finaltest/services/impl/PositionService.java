package pl.kurs.finaltest.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.finaltest.dto.PositionDto;
import pl.kurs.finaltest.dto.SimplePositionDto;
import pl.kurs.finaltest.exceptions.InvalidInputData;
import pl.kurs.finaltest.database.entity.Employee;
import pl.kurs.finaltest.database.entity.Position;
import pl.kurs.finaltest.database.repositories.PersonRepository;
import pl.kurs.finaltest.database.repositories.PositionRepository;
import pl.kurs.finaltest.services.IPositionService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class PositionService implements IPositionService {

    private PositionRepository positionRepository;
    private PersonRepository personRepository;
    private PersonService personService;
    private ModelMapper modelMapper;

    public PositionService(PositionRepository positionRepository, PersonRepository personRepository, PersonService personService, ModelMapper modelMapper) {
        this.positionRepository = positionRepository;
        this.personRepository = personRepository;
        this.personService = personService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Position addPosition(Long employeeId, SimplePositionDto positionDto) {
        Employee employee = fetchEmployeeById(employeeId);
        Position newPosition = modelMapper.map(positionDto, Position.class);

        if (checkForOverlappingPositions(employee.getId(), null, newPosition.getStartDate(), newPosition.getEndDate()) > 0) {
            throw new InvalidInputData("Daty stanowisk pokrywają się z istniejącymi stanowiskami.");
        }

        newPosition.setEmployee(employee);
        Position savedPosition = positionRepository.save(newPosition);
        personService.updateEmployeeDtoPositions(employee, personService.getEmployee(employeeId));
        return savedPosition;
    }

    @Override
    public Position updatePosition(Long positionId, PositionDto positionDto) {
        Position position = positionRepository.findPositionByIdWithOptimisticLock(positionId)
                .orElseThrow(() -> new EntityNotFoundException("Pozycja nie znaleziona: " + positionId));

        if (checkForOverlappingPositions(position.getEmployee().getId(), positionId, positionDto.getStartDate(), positionDto.getEndDate()) > 0) {
            throw new IllegalStateException("Aktualizacja pozycji skutkuje nakładaniem się dat.");
        }

        modelMapper.map(positionDto, position);
        position.setEmployee(personRepository.findEmployeeById(position.getEmployee().getId()).orElseThrow(
                () -> new EntityNotFoundException("Nie znaleziono pracownika podczas edycji pozycji")
        ));
        Position updatedPosition = positionRepository.save(position);
        personService.updateEmployeeDtoPositions(position.getEmployee(), personService.getEmployee(position.getEmployee().getId()));
        return updatedPosition;
    }

    public Set<Position> findPositionsByEmployeeId(Long employeeId) {
        List<Position> positions = positionRepository.findByEmployeeId(employeeId);

        return new HashSet<>(positions);
    }

    private long checkForOverlappingPositions(Long employeeId, Long excludePositionId, LocalDate startDate, LocalDate endDate) {
        return positionRepository.countOverlappingPositions(employeeId, excludePositionId == null ? Long.MIN_VALUE : excludePositionId, startDate, endDate);
    }

    private Employee fetchEmployeeById(Long employeeId) {
        return personRepository.findEmployeeById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono pracownika: " + employeeId));
    }
}

