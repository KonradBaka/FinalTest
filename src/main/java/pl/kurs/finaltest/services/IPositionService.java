package pl.kurs.finaltest.services;

import pl.kurs.finaltest.dto.PositionDto;
import pl.kurs.finaltest.models.Position;

import java.util.Set;

public interface IPositionService {

    Position addPosition(Long employeeId, PositionDto positionDto);

    Position updatePosition(Long positionId, PositionDto positionDto);

    Set<Position> findPositionsByEmployeeId(Long employeeId);


}
