package com.doksanbir.ratelimiter.algorithm;

import com.doksanbir.ratelimiter.model.AlgorithmType;
import com.doksanbir.ratelimiter.model.User;
import com.doksanbir.ratelimiter.config.RateLimitAlgorithm;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenBucketAlgorithm implements RateLimitAlgorithm {
    private final ConcurrentHashMap<Long, AtomicLong> tokenStorage = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, AtomicLong> lastRefillTimestampPerUser = new ConcurrentHashMap<>();
    private final long tokensPerSecond;
    private final long bucketSize;

    public TokenBucketAlgorithm(long tokensPerSecond, long bucketSize) {
        this.tokensPerSecond = tokensPerSecond;
        this.bucketSize = bucketSize;
    }

    @Override
    public boolean shouldLimitRequest(User user) {
        return !tryConsume(user); // Invert the value of tryConsume
    }

    @Override
    public boolean tryConsume(User user) {
        long userId = user.getId();
        AtomicLong userTokens = tokenStorage.computeIfAbsent(userId, k -> new AtomicLong(bucketSize));
        AtomicLong lastRefillTimestamp = lastRefillTimestampPerUser.computeIfAbsent(userId, k -> new AtomicLong(System.currentTimeMillis()));

        return userTokens.updateAndGet(tokens -> {
            long now = System.currentTimeMillis();
            long elapsedTime = now - lastRefillTimestamp.getAndSet(now);
            long tokensToAdd = (elapsedTime / 1000) * tokensPerSecond;

            tokens = Math.min(tokens + tokensToAdd, bucketSize);

            return (tokens > 0) ? tokens - 1 : 0;
        }) > 0;
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.TOKEN_BUCKET;
    }
}
