package org.example.fourchak.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.fourchak.common.annotation.LockKey;
import org.example.fourchak.common.annotation.WithRedissonLock;
import org.example.fourchak.common.error.BaseException;
import org.example.fourchak.common.error.ExceptionCode;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class WithRedissonLockAspect {

    private final RedissonClient redissonClient;
    private final AopForTransaction forTransaction;

    @Around("@annotation(org.example.fourchak.common.annotation.WithRedissonLock)")
    public void lockAroundExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("REDISSON AOP TX START: " + String.valueOf(
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
        WithRedissonLock annotation = method.getAnnotation(WithRedissonLock.class);
        log.info("REDISSON AOP TX BEFORE WHILE : " + String.valueOf(
            TransactionSynchronizationManager.isActualTransactionActive()));

        RLock rLock = redissonClient.getLock(key);
        try {
            boolean lockable = rLock.tryLock(
                annotation.waitTime(), annotation.leaseTime(),
                TimeUnit.MILLISECONDS);
            if (!lockable) {
                log.info("LOCK 획득 실패");
                return;
            }
            log.info("LOCK 획득 ID: " + key);
            // joinPoint.proceed();
            forTransaction.proceed(joinPoint);
        } catch (InterruptedException e) {
            throw new RuntimeException("스레드 인터럽트 발생");
        } finally {
            rLock.unlock();
            log.info("LOCK 해제 ID: " + key);
        }
    }
}
