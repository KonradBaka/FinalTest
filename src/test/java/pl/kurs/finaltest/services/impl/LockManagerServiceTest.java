package pl.kurs.finaltest.services.impl;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import pl.kurs.finaltest.database.entity.ImportLock;
import pl.kurs.finaltest.database.repositories.LockManagerRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class LockManagerServiceTest {

    @Autowired
    private LockManagerRepository lockManagerRepository;

    @Autowired
    private LockManagerService lockManagerService;

    @BeforeEach
    void setUp() {
        lockManagerRepository.deleteAll();
    }

//    @Test
//    @Transactional
//    void acquireLockShouldSetLockWhenNotLocked() {
//        // Given
//        String lockKey = "testKey";
//        ImportLock lock = new ImportLock(lockKey, false, null);
//        lockManagerRepository.save(lock);
//
//        // When
//        boolean result = lockManagerService.acquireLock(lockKey);
//
//        // Then
//        assertTrue(result, "Lock should be acquired");
//        ImportLock updatedLock = lockManagerRepository.findById(lockKey).orElseThrow();
//        assertTrue(updatedLock.getLocked(), "Lock should be set to true");
//        assertNotNull(updatedLock.getLockedAt(), "Lock time should be set");
//    }
//
//    @Test
//    @Transactional
//    void acquireLockShouldNotSetLockWhenAlreadyLocked() {
//        // Given
//        String lockKey = "testKey";
//        ImportLock lock = new ImportLock(lockKey, true, LocalDateTime.now());
//        lockManagerRepository.save(lock);
//
//        // When
//        boolean result = lockManagerService.acquireLock(lockKey);
//
//        // Then
//        assertFalse(result, "Lock should not be acquired");
//        ImportLock updatedLock = lockManagerRepository.findById(lockKey).orElseThrow();
//        assertTrue(updatedLock.getLocked(), "Lock should remain set to true");
//        assertNotNull(updatedLock.getLockedAt(), "Lock time should remain set");
//    }
//
//    @Test
//    @Transactional
//    void releaseLockShouldReleaseLockWhenLocked() {
//        // Given
//        String lockKey = "testKey";
//        ImportLock lock = new ImportLock(lockKey, true, LocalDateTime.now());
//        lockManagerRepository.save(lock);
//
//        // When
//        lockManagerService.releaseLock(lockKey);
//
//        // Then
//        ImportLock updatedLock = lockManagerRepository.findById(lockKey).orElseThrow();
//        assertFalse(updatedLock.getLocked(), "Lock should be released");
//        assertNull(updatedLock.getLockedAt(), "Lock time should be null");
//    }
//
//    @Test
//    @Transactional
//    void releaseLockShouldThrowExceptionWhenLockNotFound() {
//        // Given
//        String lockKey = "testKey";
//
//        // When & Then
//        assertThrows(IllegalStateException.class, () -> lockManagerService.releaseLock(lockKey));
//    }
}