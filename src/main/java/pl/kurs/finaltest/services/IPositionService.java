package pl.kurs.finaltest.services;

import pl.kurs.finaltest.dto.PositionDto;
import pl.kurs.finaltest.models.Position;

public interface IPositionService {

    Position addPosition(Long employeeId, PositionDto positionDto);

    Position updatePosition(Long positionId, PositionDto positionDto);

}
