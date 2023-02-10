package edu.gdut.web.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 日志记录
 */
@Component
@Aspect
@Slf4j
public class LogAspect {
    @Pointcut("execution(public * edu.gdut.web.controller.*.*(..))")
    public void joinPoint() {}

    @After("joinPoint()")
    public void after(JoinPoint jp) {
        String methodName = jp.getSignature().getDeclaringTypeName() + "." + jp.getSignature().getName();
        Object[] args = jp.getArgs();
        log.info("访问接口: {}, 参数: {}", methodName, Arrays.toString(args));
    }
}
