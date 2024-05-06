package pl.kurs.finaltest.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.finaltest.database.entity.ImportStatus;

public interface ImportSessionRepository extends JpaRepository<ImportStatus, Long> {
}
