package pl.kurs.finaltest.services;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class DistributedLockManager {

    private final RedisTemplate<String, String> redisTemplate;
    private final String LOCK_KEY = "FileImportLock";
    private final long LOCK_EXPIRY = 10 * 60 * 1000;


    public DistributedLockManager(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean obtainLock() {
        String value = String.valueOf(System.currentTimeMillis() + LOCK_EXPIRY);
        Boolean acquired = redisTemplate.opsForValue().setIfAbsent(LOCK_KEY, value, LOCK_EXPIRY, TimeUnit.MILLISECONDS);

        if (Boolean.TRUE.equals(acquired)) {
            return true;
        } else {
            String currentValue = redisTemplate.opsForValue().get(LOCK_KEY);
            if (currentValue != null && Long.parseLong(currentValue) < System.currentTimeMillis()) {
                String oldValue = redisTemplate.opsForValue().getAndSet(LOCK_KEY, value);
                return oldValue != null && oldValue.equals(currentValue);
            }
        }
        return false;
    }

    public void releaseLock() {
        redisTemplate.delete(LOCK_KEY);
    }
}
