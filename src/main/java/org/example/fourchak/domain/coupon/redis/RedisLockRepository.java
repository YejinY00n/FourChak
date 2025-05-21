package org.example.fourchak.domain.coupon.redis;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisLockRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public Boolean lock(Object key) {
        return redisTemplate
            .opsForValue()
            .setIfAbsent(key.toString(), "lock", Duration.ofMillis(3000));  // 3초 동안 락 유효
    }

    public Boolean unlock(Object key) {
        return redisTemplate.delete(key.toString());
    }
}
