package com.hifive.bururung.global.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class LogAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Before 실행 - 입력 값이 있을 경우만 출력
    @Before("execution(* com.hifive.bururung..controller..*(..))")
    public void beforeLog(JoinPoint joinPoint) {
    	String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        // 입력 값이 있는 경우만 로그 출력
        if (args.length > 0) {
            String params = Arrays.stream(args)
                    .map(arg -> {
                        try {
                            return objectMapper.writeValueAsString(arg);
                        } catch (Exception e) {
                            return arg.toString();
                        }
                    })
                    .collect(Collectors.joining(", "));
            log.info("[[[AOP-before log]]] - 호출된 메서드: {}, 파라미터: {}", className, methodName, params);
        } else {
            log.info("[[[AOP-before log]]] - 호출된 메서드: {}",className, methodName);
        }
    }

    // AfterReturning 실행 - 정상 실행 후 반환값 로깅
    @AfterReturning(pointcut = "execution(* com.hifive.bururung..controller..*(..))", returning = "result")
    public void afterReturningLog(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        try {
            String resultJson = objectMapper.writeValueAsString(result);
            log.info("[[[AOP-afterReturning log]]] - 실행된 메서드: {}, 반환값: {}", methodName, resultJson);
        } catch (Exception e) {
            log.warn("[[[AOP-afterReturning log]]] - 실행된 메서드: {}, 반환값 변환 실패", methodName);
        }
    }

    // AfterThrowing 실행 - 예외 발생 시 로그 남김
    @AfterThrowing(pointcut = "execution(* com.hifive.bururung..controller..*(..))", throwing = "ex")
    public void afterThrowingLog(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        log.error("[[[AOP-afterThrowing log]]] - 실행된 메서드: {}, 예외 발생: {}", methodName, ex.getMessage());
    }

    // Around 실행 - 실행 시간 측정
    @Around("execution(* com.hifive.bururung..controller..*(..))")
    public Object aroundLog(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed(); // 메서드 실행

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        log.info("[[[AOP-around log]]] - 실행된 메서드: {}, 실행 시간: {}ms", methodName, executionTime);

        return result;
    }
}
