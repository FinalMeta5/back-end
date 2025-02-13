package com.hifive.bururung.global.common;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class LogAspect {
	@Before("execution(* com.hifive.bururung..CarRegistrationController.*(..))")
	public void beforeLog(JoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		String methodName = signature.getName();
		System.out.println("-----------------------");
		log.info("[[[AOP-before log]]] - {}", methodName);
	}
	
	

}
