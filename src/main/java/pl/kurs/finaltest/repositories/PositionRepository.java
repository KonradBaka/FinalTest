package pl.kurs.finaltest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.models.Position;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface PositionRepository extends JpaRepository<Position, Long> {

    List<Position> findByEmployeeId(Long employeeId);

    @Query("SELECT COUNT(p) FROM Position p WHERE p.employee.id = :employeeId AND p.id <> :excludePositionId AND NOT(p.endDate < :startDate OR p.startDate > :endDate)")
    Long countOverlappingPositions(@Param("employeeId") Long employeeId, @Param("excludePositionId") Long excludePositionId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
