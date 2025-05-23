package org.example.fourchak.domain.coupon.lock.redis;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fourchak.common.error.BaseException;
import org.example.fourchak.common.error.ExceptionCode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisLockRepository {

    private final RedisTemplate<String, String> redisTemplate;


    public Boolean lock(Object key) {
        return redisTemplate
            .opsForValue()
            .setIfAbsent(key.toString(), "lock", Duration.ofMillis(3000));  // 3초 동안 락 유효
    }

    public void lockLimitTry(String key, int maxRetry) {
        int retry = 0;

        while (!lock(key)) {
            log.info("LOCK 획득 실패");
            if (++retry == maxRetry) {
                throw new BaseException(ExceptionCode.LOCK_EXCEPTION);
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void unlock(Object key) {
        if (redisTemplate.hasKey(key.toString())) {
            redisTemplate.delete(key.toString());
        }
    }
}
