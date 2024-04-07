package pl.kurs.finaltest.services;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.finaltest.dto.PositionDto;
import pl.kurs.finaltest.models.Employee;
import pl.kurs.finaltest.models.Position;
import pl.kurs.finaltest.repositories.PersonRepository;
import pl.kurs.finaltest.repositories.PositionRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class PositionService implements IPositionService {

    private PositionRepository positionRepository;
    private PersonRepository personRepository;
    private ModelMapper modelMapper;

    public PositionService(PositionRepository positionRepository, PersonRepository personRepository, ModelMapper modelMapper) {
        this.positionRepository = positionRepository;
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Position addPosition(Long employeeId, PositionDto positionDto) {
        Employee employee = fetchEmployeeById(employeeId);
        Position newPosition = modelMapper.map(positionDto, Position.class);

        if (checkForOverlappingPositions(employee.getId(), null, newPosition.getStartDate(), newPosition.getEndDate()) > 0) {
            throw new IllegalStateException("Daty stanowisk pokrywają się z istniejącymi stanowiskami.");
        }

        newPosition.setEmployee(employee);
        return positionRepository.save(newPosition);
    }

    @Override
    public Position updatePosition(Long positionId, PositionDto positionDto) {
        Position position = positionRepository.findPositionByIdWithPessimisticLock(positionId)
                .orElseThrow(() -> new EntityNotFoundException("Pozycja nie znaleziona: " + positionId));

        if (checkForOverlappingPositions(position.getEmployee().getId(), positionId, positionDto.getStartDate(), positionDto.getEndDate()) > 0) {
            throw new IllegalStateException("Aktualizacja pozycji skutkuje nakładaniem się dat.");
        }
        modelMapper.map(positionDto, position);
        return positionRepository.save(position);
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

