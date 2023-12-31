package com.doksanbir.ratelimiter.config;

import com.doksanbir.ratelimiter.model.AlgorithmType;
import com.doksanbir.ratelimiter.model.User;

public interface RateLimitAlgorithm {
    boolean shouldLimitRequest(User user);
    boolean canConsume(User user);
    AlgorithmType getAlgorithmType();
}


