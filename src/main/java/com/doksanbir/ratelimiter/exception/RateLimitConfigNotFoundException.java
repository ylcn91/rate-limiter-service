package com.doksanbir.ratelimiter.exception;

public class RateLimitConfigNotFoundException extends RuntimeException {

    public RateLimitConfigNotFoundException(String message) {
        super(message);
    }
}

