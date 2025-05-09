package org.example.expert.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.log.service.LoggingService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LoggingAspect {

    private final LoggingService loggingService;
    private final HttpServletRequest request;

    @Around("execution(* org.example.expert.domain.manager.controller.ManagerController.saveManager(..))")
    public Object logTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String requestUrl = request.getRequestURI();
        LocalDateTime requestTime = LocalDateTime.now();
        String actionMethod = String.valueOf(joinPoint.getSignature().getName());

        Object result = null;
        try {
            result = joinPoint.proceed();
            loggingService.saveLog(requestTime, actionMethod, requestUrl, true, authUser.getId(), "");
            log.info("Admin Access Log - User ID: {}, Request Time: {}, Request URL: {}, Method: {}, success: {}",
                    authUser.getId(), requestTime, requestUrl, actionMethod, true);
        } catch (Exception ex) {
            loggingService.saveLog(requestTime, actionMethod, requestUrl, false, authUser.getId(), ex.getMessage());
            log.info("Admin Access Log - User ID: {}, Request Time: {}, Request URL: {}, Method: {}, success: {}, error message: {}",
                    authUser.getId(), requestTime, requestUrl, actionMethod, false, ex.getMessage());
            throw ex;
        }
        return result;
    }
}