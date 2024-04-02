package pl.kurs.finaltest.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import pl.kurs.finaltest.dto.PersonDto;
import pl.kurs.finaltest.dto.PositionDto;
import pl.kurs.finaltest.entityspecification.SearchCriteria;
import pl.kurs.finaltest.models.Employee;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.models.Position;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface IPositionService {

    Position addPosition(Long employeeId, PositionDto positionDto);

    Position updatePosition(Long positionId, PositionDto positionDto);

    Set<Position> findPositionsByEmployeeId(Long employeeId);


}
