package org.example.fourchak.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.fourchak.common.annotation.LockKey;
import org.example.fourchak.common.error.BaseException;
import org.example.fourchak.common.error.ExceptionCode;
import org.example.fourchak.domain.coupon.lock.RedisLockRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;


@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class WithRedisLockAspect {

    private final RedisLockRepository redisLockRepository;

    @Around("@annotation(org.example.fourchak.common.annotation.WithLock)")
    public Object lockAroundExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("AOP TX START: " + String.valueOf(
            TransactionSynchronizationManager.isActualTransactionActive()));
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        Annotation[][] paramAnnotations = method.getParameterAnnotations();

        Object lockKeyValue = null;
        for (int i = 0; i < paramAnnotations.length; i++) {
            for (Annotation annotation : paramAnnotations[i]) {
                if (annotation.annotationType() == LockKey.class) {
                    lockKeyValue = args[i];
                    break;
                }
            }
        }
        // 호출한 메소드 파라미터에 @LockKey 가 없으면 오류 발생
        if (lockKeyValue == null) {
            throw new BaseException(ExceptionCode.INTERNAL_SERVER_ERROR);
        }

        String key = method.getName() + ":" + lockKeyValue;

        log.info("AOP TX BEFORE WHILE : " + String.valueOf(
            TransactionSynchronizationManager.isActualTransactionActive()));
        while (!redisLockRepository.lock(key)) {
            log.info("LOCK 획득 실패");
            Thread.sleep(100);      // 100ms 대기 후 재시도
        }

        // 락 획득에 성공하면 로직 실행
        log.info("LOCK 획득 ID: " + key);
        try {
//            return joinPoint.proceed();
            Object result = joinPoint.proceed();
            TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronizationAdapter() {
                    @Override
                    public void afterCommit() {
                        redisLockRepository.unlock(key); // 트랜잭션 커밋 이후에 락 해제
                        log.info("LOCK 해제 ID: " + key);
                    }
                });

            return result;
        } finally {
//            log.info("AOP TX FINALLY : " + String.valueOf(
//                TransactionSynchronizationManager.isActualTransactionActive()));
//            // 로직이 모두 수행되었다면 Lock 해제
//            redisLockRepository.unlock(key);
//            log.info("LOCK 해제 ID: " + key);
        }
    }
}
