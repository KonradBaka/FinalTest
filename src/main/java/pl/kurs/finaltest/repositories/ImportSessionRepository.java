package pl.kurs.finaltest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.finaltest.models.ImportStatus;

import java.util.Optional;

public interface ImportSessionRepository extends JpaRepository<ImportStatus, Long> {

    Optional<ImportStatus> findTopByOrderByStartTimeDesc();

}
