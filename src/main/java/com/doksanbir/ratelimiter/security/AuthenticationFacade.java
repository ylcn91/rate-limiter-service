package com.doksanbir.ratelimiter.security;

import com.doksanbir.ratelimiter.exception.UnauthenticatedUserException;
import com.doksanbir.ratelimiter.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationFacade {

    public User getAuthenticatedUser() throws UnauthenticatedUserException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.user();
        }
        throw new UnauthenticatedUserException("User is not authenticated");
    }
}

