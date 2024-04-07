package pl.kurs.finaltest.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kurs.finaltest.dto.PositionDto;
import pl.kurs.finaltest.models.Employee;
import pl.kurs.finaltest.models.Position;

import java.util.Optional;
import java.util.Set;

public interface IPositionService {

    Position addPosition(Long employeeId, PositionDto positionDto);

    Position updatePosition(Long positionId, PositionDto positionDto);

}
