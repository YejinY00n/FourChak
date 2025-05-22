package org.example.fourchak.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RedissonLock {

    String key();

    long waitTime() default 5000;

    long leaseTime() default 3000;

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
