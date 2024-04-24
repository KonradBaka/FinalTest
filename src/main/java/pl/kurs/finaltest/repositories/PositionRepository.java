package pl.kurs.finaltest.repositories;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kurs.finaltest.models.Position;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {

    @Query("SELECT p FROM Position p LEFT JOIN FETCH p.employee WHERE p.employee.id = :employeeId")
    List<Position> findByEmployeeId(Long employeeId);

    @Query("SELECT COUNT(p) FROM Position p WHERE p.employee.id = :employeeId AND p.id <> :excludePositionId AND NOT(p.endDate < :startDate OR p.startDate > :endDate)")
    Long countOverlappingPositions(@Param("employeeId") Long employeeId, @Param("excludePositionId") Long excludePositionId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT p FROM Position p WHERE p.id = :id")
     Optional<Position> findPositionByIdWithOptimisticLock(@Param("id") Long id);


}
