package com.doksanbir.ratelimiter.service;

import com.doksanbir.ratelimiter.exception.RateLimitConfigNotFoundException;
import com.doksanbir.ratelimiter.model.RateLimitConfig;
import com.doksanbir.ratelimiter.model.User;

import java.util.List;
import java.util.Optional;

import java.util.List;
import java.util.Optional;

public interface RateLimitConfigService {

    RateLimitConfig saveRateLimitConfig(RateLimitConfig rateLimitConfig);

    Optional<RateLimitConfig> getRateLimitConfigById(Long id);

    List<RateLimitConfig> getAllRateLimitConfigs();

    void deleteRateLimitConfig(Long id);

    Optional<RateLimitConfig> getRateLimitConfigByUser(User user);
}

