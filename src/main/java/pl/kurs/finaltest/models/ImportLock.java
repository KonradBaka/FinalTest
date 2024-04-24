package pl.kurs.finaltest.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "import_locks")
public class ImportLock {

    @Id
    private String lockKey;
    private Boolean locked;
    private LocalDateTime lockedAt;

    public ImportLock() {
    }

    public ImportLock(String lockKey, Boolean locked, LocalDateTime lockedAt) {
        this.lockKey = lockKey;
        this.locked = locked;
        this.lockedAt = lockedAt;
    }

    public String getLockKey() {
        return lockKey;
    }

    public void setLockKey(String lockKey) {
        this.lockKey = lockKey;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public LocalDateTime getLockedAt() {
        return lockedAt;
    }

    public void setLockedAt(LocalDateTime lockedAt) {
        this.lockedAt = lockedAt;
    }
}
