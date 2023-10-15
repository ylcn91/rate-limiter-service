package com.doksanbir.ratelimiter.aspect;

import com.doksanbir.ratelimiter.annotation.RateLimited;
import com.doksanbir.ratelimiter.config.RateLimitAlgorithm;
import com.doksanbir.ratelimiter.exception.RateLimitExceededException;
import com.doksanbir.ratelimiter.exception.UnauthenticatedUserException;
import com.doksanbir.ratelimiter.model.AlgorithmType;
import com.doksanbir.ratelimiter.model.User;
import com.doksanbir.ratelimiter.security.AuthenticationFacade;
import com.doksanbir.ratelimiter.security.CustomUserDetails;
import com.doksanbir.ratelimiter.service.RateLimitAlgorithmResolver;
import com.doksanbir.ratelimiter.validator.AlgorithmConfigValidator;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Method;


import com.doksanbir.ratelimiter.exception.InvalidAlgorithmConfigurationException;


@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitedAspect {

    private final RateLimitAlgorithmResolver rateLimitAlgorithmResolver;
    private final AlgorithmConfigValidator algorithmConfigValidator;
    private final AuthenticationFacade authenticationFacade;

    @Around("@annotation(com.doksanbir.ratelimiter.annotation.RateLimited)")
    public Object rateLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RateLimited rateLimited = method.getAnnotation(RateLimited.class);

        // Validate algorithm configuration
        try {
            algorithmConfigValidator.validate(rateLimited);
        } catch (InvalidAlgorithmConfigurationException e) {
            // Handle the exception appropriately
            throw new RateLimitExceededException("Invalid algorithm configuration");
        }

        AlgorithmType type = rateLimited.algorithmType();
        RateLimitAlgorithm algorithm = rateLimitAlgorithmResolver.getAlgorithm(type);

        User user = getUserFromContext();
        if (user != null && algorithm.canConsume(user)) {
            return joinPoint.proceed();
        } else {
            throw new RateLimitExceededException("Rate limit exceeded");
        }
    }

    private User getUserFromContext() throws UnauthenticatedUserException {
        return authenticationFacade.getAuthenticatedUser();
    }

}


