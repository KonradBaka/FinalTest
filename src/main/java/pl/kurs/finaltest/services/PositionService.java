package pl.kurs.finaltest.services;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.finaltest.dto.PositionDto;
import pl.kurs.finaltest.models.Employee;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.models.Position;
import pl.kurs.finaltest.repositories.PersonRepository;
import pl.kurs.finaltest.repositories.PositionRepository;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class PositionService implements IPositionService {

    private PositionRepository positionRepository;
    private PersonRepository personRepository;
    private ModelMapper modelMapper;

    public PositionService(PositionRepository positionRepository, PersonRepository personRepository, ModelMapper modelMapper) {
        this.positionRepository = positionRepository;
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
    }


    public Position addPosition(Long employeeId, PositionDto positionDto) {
        Employee employee = fetchEmployeeById(employeeId);

        Position newPosition = modelMapper.map(positionDto, Position.class);
        if (checkPositionOverlap(employee, newPosition)) {
            throw new IllegalStateException("Daty nowych stanowisk pokrywają się z istniejącymi stanowiskami dla tego pracownika.");
        }

        newPosition.setEmployee(employee);
        return positionRepository.save(newPosition);
    }

    private Employee fetchEmployeeById(Long employeeId) {
        Person person = personRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono pracownika " + employeeId));
        if (!(person instanceof Employee)) {
            throw new IllegalArgumentException("Podany identyfikator nie należy do pracownika.");
        }
        return (Employee) person;
    }



    public Position updatePosition(Long positionId, PositionDto positionDto) {
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new EntityNotFoundException("Pozycja nie została znaleziona: " + positionId));

        LocalDate originalStartDate = position.getStartDate();
        LocalDate originalEndDate = position.getEndDate();

        modelMapper.map(positionDto, position);

        Long overlappingCount = positionRepository.countOverlappingPositions(position.getEmployee().getId(), positionId, position.getStartDate(), position.getEndDate());
        if (overlappingCount > 0) {
            position.setStartDate(originalStartDate);
            position.setEndDate(originalEndDate);
            throw new IllegalStateException("Aktualizacja pozycji powoduje nakładanie się na istniejące pozycje.");
        }

        return positionRepository.save(position);
    }

    public Set<Position> findPositionsByEmployeeId(Long employeeId) {
        Employee employee = personRepository.findEmployeeById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znleziono pracownika" + employeeId));

        return employee.getPositions();
    }


    private boolean checkPositionOverlap(Employee employee, Position newPosition) {
        for (Position position : employee.getPositions()) {
            boolean startsDuringAnotherPosition = !newPosition.getStartDate().isBefore(position.getStartDate()) && newPosition.getStartDate().isBefore(position.getEndDate());
            boolean endsDuringAnotherPosition = !newPosition.getEndDate().isAfter(position.getEndDate()) && newPosition.getEndDate().isAfter(position.getStartDate());
            boolean encompassesAnotherPosition = newPosition.getStartDate().isBefore(position.getStartDate()) && newPosition.getEndDate().isAfter(position.getEndDate());

            if (startsDuringAnotherPosition || endsDuringAnotherPosition || encompassesAnotherPosition) {
                return true;
            }
        }
        return false;
    }
}

