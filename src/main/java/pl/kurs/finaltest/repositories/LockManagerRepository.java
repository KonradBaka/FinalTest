package pl.kurs.finaltest.repositories;

import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.finaltest.models.ImportLock;

public interface LockManagerRepository extends JpaRepository<ImportLock, String> {

    @Transactional
    @Modifying
    @Query("UPDATE ImportLock l SET l.locked = TRUE, l.lockedAt = CURRENT_TIMESTAMP WHERE l.lockKey = :lockKey AND l.locked = FALSE")
    int acquireLock(String lockKey);

    @Transactional
    @Modifying
    @Query("UPDATE ImportLock l SET l.locked = FALSE, l.lockedAt = NULL WHERE l.lockKey = :lockKey")
    void releaseLock(String lockKey);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM ImportLock l WHERE l.lockKey = :lockKey")
    ImportLock getLock(String lockKey);

}
