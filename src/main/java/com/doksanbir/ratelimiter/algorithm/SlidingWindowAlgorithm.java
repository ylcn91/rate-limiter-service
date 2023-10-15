package com.doksanbir.ratelimiter.algorithm;

import com.doksanbir.ratelimiter.exception.RateLimitExceededException;
import com.doksanbir.ratelimiter.model.AlgorithmType;
import com.doksanbir.ratelimiter.model.User;
import com.doksanbir.ratelimiter.config.RateLimitAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;


@Slf4j
public class SlidingWindowAlgorithm implements RateLimitAlgorithm {
    private final ConcurrentHashMap<Long, ConcurrentLinkedQueue<Long>> timestamps = new ConcurrentHashMap<>();
    private final long timeWindowInMillis;
    private final int maxRequests;

    public SlidingWindowAlgorithm(long timeWindowInMillis, int maxRequests) {
        this.timeWindowInMillis = timeWindowInMillis;
        this.maxRequests = maxRequests;
    }

    @Override
    public boolean shouldLimitRequest(User user) {
        return canConsume(user);
    }

    @Override
    public boolean canConsume(User user) {
        long currentTime = System.currentTimeMillis();
        AtomicBoolean shouldAllowRequest = new AtomicBoolean(true);

        timestamps.compute(user.getId(), (key, userTimestamps) -> {
            if (userTimestamps == null) {
                userTimestamps = new ConcurrentLinkedQueue<>();
            }

            while (!userTimestamps.isEmpty() && userTimestamps.poll() < currentTime - timeWindowInMillis) {
            }

            if (userTimestamps.size() >= maxRequests) {
                shouldAllowRequest.set(false);
            } else {
                userTimestamps.add(currentTime);
            }

            return userTimestamps;
        });

        if (!shouldAllowRequest.get()) {
            try {
                log.warn("Rate limit exceeded for user: {}", user.getId());
            } catch (Exception e) {
                // Log the failure to the same or a different logger
                log.error("Logging failed: ", e);
            }
            throw new RateLimitExceededException("Rate limit exceeded");
        }

        return !shouldAllowRequest.get();
    }


    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.SLIDING_WINDOW;
    }
}
