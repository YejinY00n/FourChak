package org.example.fourchak.domain.reservation.service;

import java.time.Duration;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DistributedLockService {

    private final StringRedisTemplate stringRedisTemplate;

    public boolean tryLock(String key, String value, Duration timeout) {

        Boolean success = stringRedisTemplate.opsForValue().setIfAbsent(key, value, timeout);
        return Boolean.TRUE.equals(success);
    }

    public boolean releaseLock(String key, String value) {
        String script =
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                "   return redis.call('del', KEYS[1]) " +
                "else " +
                "   return 0 " +
                "end";
        Long result = stringRedisTemplate.execute(
            new DefaultRedisScript<>(script, Long.class),
            Collections.singletonList(key),
            value
        );
        return result != null && result == 1L;
    }

}
