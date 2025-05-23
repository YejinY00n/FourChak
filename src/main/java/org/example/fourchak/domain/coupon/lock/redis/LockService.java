package org.example.fourchak.domain.coupon.lock.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LockService {

    private final RedisLockRepository redisLockRepository;

    // 스핀락 방식
    public void executeWithLock(String key, Runnable logic) throws InterruptedException {
        // Lock 획득
        while (!redisLockRepository.lock(key)) {
            log.info("LOCK 획득 실패");
            Thread.sleep(100);      // 100ms 대기 후 재시도
        }

        // 락 획득에 성공하면 유저 쿠폰 발급
        log.info("LOCK 획득 ID: " + key);
        try {
            logic.run();
        } finally {
            // 로직이 모두 수행되었다면 Lock 해제
            redisLockRepository.unlock(key);
            log.info("LOCK 해제 ID: " + key);
        }
    }
}
