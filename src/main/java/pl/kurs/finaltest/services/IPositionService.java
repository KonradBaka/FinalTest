package pl.kurs.finaltest.services;

import pl.kurs.finaltest.dto.PositionDto;
import pl.kurs.finaltest.dto.SimplePositionDto;
import pl.kurs.finaltest.database.entity.Position;

public interface IPositionService {

    Position addPosition(Long employeeId, SimplePositionDto positionDto);

    Position updatePosition(Long positionId, PositionDto positionDto);

}
