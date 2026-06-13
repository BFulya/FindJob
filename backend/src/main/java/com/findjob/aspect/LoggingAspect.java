package com.findjob.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Logging Aspect using AOP
 * Implements Cross-Cutting Concerns pattern
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("execution(* com.findjob.service..*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        
        log.info("Entering {}.{}() with arguments: {}", className, methodName, joinPoint.getArgs());
        
        long startTime = System.currentTimeMillis();
        
        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            
            log.info("Exiting {}.{}() in {} ms", className, methodName, (endTime - startTime));
            
            return result;
        } catch (Exception e) {
            log.error("Exception in {}.{}(): {}", className, methodName, e.getMessage());
            throw e;
        }
    }

    @Around("execution(* com.findjob.controller..*(..))")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        
        log.info("API Request: {}.{}()", className, methodName);
        
        long startTime = System.currentTimeMillis();
        
        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            
            log.info("API Response: {}.{}() completed in {} ms", className, methodName, (endTime - startTime));
            
            return result;
        } catch (Exception e) {
            log.error("API Error in {}.{}(): {}", className, methodName, e.getMessage());
            throw e;
        }
    }
}
