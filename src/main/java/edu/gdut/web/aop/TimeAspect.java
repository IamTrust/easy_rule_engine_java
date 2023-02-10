package edu.gdut.web.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 接口耗时统计
 */
@Component
@Aspect
@Slf4j
public class TimeAspect {
    @Pointcut("execution(public * edu.gdut.web.controller.*.*(..))")
    public void joinPoint() {}

    @Around("joinPoint()")
    public Object around(ProceedingJoinPoint pjp) {
        Object obj = null;
        try {
            long begin = System.currentTimeMillis();
            obj = pjp.proceed();
            long end = System.currentTimeMillis();
            log.info("{} 方法耗时: {} ms", pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName(), end-begin);
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        return obj;
    }
}
