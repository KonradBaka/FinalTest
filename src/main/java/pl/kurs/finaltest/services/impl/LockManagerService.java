package pl.kurs.finaltest.services.impl;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import pl.kurs.finaltest.database.entity.ImportLock;
import pl.kurs.finaltest.database.repositories.LockManagerRepository;

import java.time.LocalDateTime;

@Service
public class LockManagerService {

    private LockManagerRepository lockManagerRepository;

    public LockManagerService(LockManagerRepository lockManagerRepository) {
        this.lockManagerRepository = lockManagerRepository;
    }

    public boolean acquireLock(String lockKey) {
        try {
            ImportLock lock = lockManagerRepository.findById(lockKey)
                    .orElse(new ImportLock(lockKey, false, null));

            if (!lock.getLocked()) {
                lock.setLocked(true);
                lock.setLockedAt(LocalDateTime.now());
                lockManagerRepository.save(lock);
                System.out.println("-ZABLOKOWANO-");
                return true;
            }
            return false;
        } catch (ObjectOptimisticLockingFailureException e) {
            return false;
        }
    }

    public void releaseLock(String lockKey) {
        ImportLock lock = lockManagerRepository.findById(lockKey)
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono locka: " + lockKey));

        lock.setLocked(false);
        lock.setLockedAt(null);
        lockManagerRepository.save(lock);
        System.out.println("-ODBLOKOWANO-");
    }

}
