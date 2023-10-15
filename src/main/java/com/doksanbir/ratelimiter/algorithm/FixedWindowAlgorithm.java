package com.doksanbir.ratelimiter.algorithm;

import com.doksanbir.ratelimiter.exception.RateLimitExceededException;
import com.doksanbir.ratelimiter.model.AlgorithmType;
import com.doksanbir.ratelimiter.model.User;
import com.doksanbir.ratelimiter.config.RateLimitAlgorithm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.LongAdder;

@Slf4j
@Component
public class FixedWindowAlgorithm implements RateLimitAlgorithm {

    private final ConcurrentMap<Long, LongAdder> requestCounts = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, Long> windowStartTimes = new ConcurrentHashMap<>();
    private static final long TIME_WINDOW = 60000; // 1 minute in milliseconds
    private static final int MAX_REQUESTS = 10; // Max requests per minute

    @Override
    public boolean shouldLimitRequest(User user) {
        long userId = user.getId();
        long currentTime = System.currentTimeMillis();

        windowStartTimes.computeIfAbsent(userId, k -> currentTime);
        requestCounts.computeIfAbsent(userId, k -> new LongAdder());

        long windowStartTime = windowStartTimes.get(userId);
        long elapsedTime = currentTime - windowStartTime;

        if (elapsedTime > TIME_WINDOW) {
            windowStartTimes.put(userId, currentTime);
            requestCounts.get(userId).reset();
            return false;
        } else {
            return requestCounts.get(userId).intValue() >= MAX_REQUESTS;
        }
    }

    @Override
    public boolean canConsume(User user) throws RateLimitExceededException {
        if (shouldLimitRequest(user)) {
            try {
                log.warn("Rate limit exceeded for user {}", user.getId());
            } catch (Exception e) {
                log.error("Logging failed: ", e);
            }
            throw new RateLimitExceededException("Rate limit exceeded");
        } else {
            requestCounts.get(user.getId()).increment();
            return false;
        }
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.FIXED_WINDOW;
    }
}
