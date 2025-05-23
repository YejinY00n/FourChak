package org.example.fourchak.common.annotation;

import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.fourchak.common.error.CustomRuntimeException;
import org.example.fourchak.common.error.ExceptionCode;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class RedissonLockAspect {

    private final RedissonClient redissonClient;
    private final SpelExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(redissonLock)")
    public Object applyRedissonLock(ProceedingJoinPoint joinPoint, RedissonLock redissonLock)
        throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // SpEL 키 평가
        EvaluationContext context = new StandardEvaluationContext();
        String[] paramNames = nameDiscoverer.getParameterNames(method);
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }

        String evaluatedKey = parser.parseExpression(redissonLock.key())
            .getValue(context, String.class);
        RLock lock = redissonClient.getLock(evaluatedKey);

        boolean acquired = false;
        try {
            acquired = lock.tryLock(
                redissonLock.waitTime(),
                redissonLock.leaseTime(),
                redissonLock.timeUnit()
            );

            if (!acquired) {
                throw new CustomRuntimeException(ExceptionCode.LOCK_EXCEPTION);
            }

            return joinPoint.proceed();
        } finally {
            if (acquired && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
