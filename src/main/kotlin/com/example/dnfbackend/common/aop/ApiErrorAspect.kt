package com.example.dnfbackend.common.aop

import com.example.dnfbackend.common.exception.ApiFailureException
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component

@Aspect
@Component
class ApiErrorAspect {

    @Pointcut("execution(* com.example.dnfbackend.api.service.DnfApiService.*(..))")
    fun apiErrorPointcut() {
    }

    @Around("apiErrorPointcut()")
    fun apiErrorAround(pjp: ProceedingJoinPoint): Any? {
        val proceed: Any?
        try {
            proceed = pjp.proceed()
        } catch (e: Exception) {
            throw ApiFailureException("API 호출 중 오류가 발생했습니다. ${e.message}", e)
        }
        return proceed
    }

}
