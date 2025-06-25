package com.jmp.gestion_notes.logging;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.UUID;

@Aspect
@Component
public class ApplicationLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger("APP_JOURNAL");

    // Log ALL Controller methods - This catches every user action
    // ← CHANGE: Replace "com.yourpackage.controller" with your actual controller package
    @Around("execution(* com.jmp.gestion_notes.controller..*(..))")
    public Object logControllerActions(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();
        
        // Get current user
        String currentUser = getCurrentUser();
        String correlationId = UUID.randomUUID().toString().substring(0, 8);
        
        // Set MDC for structured logging
        MDC.put("user", currentUser);
        MDC.put("action", methodName);
        MDC.put("controller", className);
        MDC.put("correlationId", correlationId);
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Log the action START
            logger.info("🚀 USER ACTION START - User: {} | Action: {}.{} | Parameters: {}", 
                       currentUser, className, methodName, 
                       sanitizeParameters(args));
            
            // Execute the method
            Object result = joinPoint.proceed();
            
            long duration = System.currentTimeMillis() - startTime;
            
            // Log the action SUCCESS
            logger.info("✅ USER ACTION SUCCESS - User: {} | Action: {}.{} | Duration: {}ms", 
                       currentUser, className, methodName, duration);
            
            return result;
            
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            
            // Log the action FAILURE
            logger.error("❌ USER ACTION FAILED - User: {} | Action: {}.{} | Duration: {}ms | Error: {}", 
                        currentUser, className, methodName, duration, e.getMessage());
            
            throw e;
        } finally {
            MDC.clear();
        }
    }

    // Log important SERVICE methods (business logic)
    // ← CHANGE: Replace "com.yourpackage.service" with your actual service package
    @Around("execution(* com.yourpackage.service..*(..))")
    public Object logServiceOperations(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        // Only log important service methods (not getters)
        if (isImportantServiceMethod(methodName)) {
            String currentUser = getCurrentUser();
            
            try {
                logger.info("🔧 SERVICE OPERATION - User: {} | Service: {}.{}", 
                           currentUser, className, methodName);
                
                Object result = joinPoint.proceed();
                
                logger.info("🔧 SERVICE COMPLETED - User: {} | Service: {}.{}", 
                           currentUser, className, methodName);
                
                return result;
                
            } catch (Exception e) {
                logger.error("🔧 SERVICE FAILED - User: {} | Service: {}.{} | Error: {}", 
                            currentUser, className, methodName, e.getMessage());
                throw e;
            }
        } else {
            return joinPoint.proceed();
        }
    }

    // Get current authenticated user
    private String getCurrentUser() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
                return auth.getName();
            }
            return "anonymous";
        } catch (Exception e) {
            return "unknown";
        }
    }

    // Clean parameters for logging (remove sensitive data)
    private String sanitizeParameters(Object[] args) {
        if (args == null || args.length == 0) {
            return "[]";
        }
        
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                sb.append("null");
            } else {
                String className = args[i].getClass().getSimpleName();
                
                // Skip logging HTTP request objects
                if (className.contains("Request") || className.contains("Response")) {
                    sb.append("HttpRequest/Response");
                } else if (args[i].toString().toLowerCase().contains("password")) {
                    sb.append("***HIDDEN***");
                } else {
                    String argStr = args[i].toString();
                    // Limit parameter length
                    sb.append(argStr.length() > 100 ? argStr.substring(0, 100) + "..." : argStr);
                }
            }
            
            if (i < args.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // Determine if service method is important to log
    private boolean isImportantServiceMethod(String methodName) {
        String method = methodName.toLowerCase();
        return method.startsWith("save") || 
               method.startsWith("create") || 
               method.startsWith("update") || 
               method.startsWith("delete") || 
               method.startsWith("remove") || 
               method.startsWith("add") || 
               method.startsWith("process") || 
               method.startsWith("execute") ||
               method.startsWith("send") ||
               method.startsWith("generate");
    }
}