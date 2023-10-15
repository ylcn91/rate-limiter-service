package com.doksanbir.ratelimiter.algorithm;

import com.doksanbir.ratelimiter.exception.RateLimitExceededException;
import com.doksanbir.ratelimiter.model.AlgorithmType;
import com.doksanbir.ratelimiter.model.User;
import com.doksanbir.ratelimiter.config.RateLimitAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;


@Slf4j
@Component
public class SlidingWindowAlgorithm implements RateLimitAlgorithm {
    private final ConcurrentHashMap<Long, ConcurrentLinkedQueue<Long>> timestamps = new ConcurrentHashMap<>();
    @Value("${slidingWindow.timeWindowInMillis}")
    private long timeWindowInMillis;

    @Value("${slidingWindow.maxRequests}")
    private int maxRequests;

    public SlidingWindowAlgorithm() {
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
