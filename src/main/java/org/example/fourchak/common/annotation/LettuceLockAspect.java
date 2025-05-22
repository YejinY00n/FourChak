package org.example.fourchak.common.annotation;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.fourchak.common.error.CustomRuntimeException;
import org.example.fourchak.common.error.ExceptionCode;
import org.example.fourchak.domain.reservation.service.DistributedLockService;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class LettuceLockAspect {

    private final DistributedLockService lockService;
    private final SpelExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(lettuceLock)")
    public Object applyLock(ProceedingJoinPoint joinPoint, LettuceLock lettuceLock)
        throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // SpEL 키 평가
        EvaluationContext context = new StandardEvaluationContext();
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }

        String evaluatedKey = parser.parseExpression(lettuceLock.key())
            .getValue(context, String.class);
        String value = UUID.randomUUID().toString();

        Duration timeout = Duration.ofMillis(lettuceLock.timeout());
        Duration interval = Duration.ofMillis(100); // 100ms 간격으로 재시도
        long deadline = System.currentTimeMillis() + timeout.toMillis();
        boolean locked = false;

        while (System.currentTimeMillis() < deadline) {
            locked = lockService.tryLock(evaluatedKey, value, timeout);
            if (locked) {
                break;
            }
            Thread.sleep(interval.toMillis());
        }

        if (!locked) {
            throw new CustomRuntimeException(ExceptionCode.LOCK_EXCEPTION);
        }

        try {
            return joinPoint.proceed();
        } finally {
            lockService.releaseLock(evaluatedKey, value);
        }
    }
}

