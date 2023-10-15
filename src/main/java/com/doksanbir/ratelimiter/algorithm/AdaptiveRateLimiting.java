package com.doksanbir.ratelimiter.algorithm;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

import com.doksanbir.ratelimiter.model.AlgorithmType;
import com.doksanbir.ratelimiter.model.User;
import com.doksanbir.ratelimiter.config.RateLimitAlgorithm;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdaptiveRateLimiting implements RateLimitAlgorithm {

    private final ConcurrentHashMap<Long, AtomicLong> lastAccessTime = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, LongAdder> requestCount = new ConcurrentHashMap<>();
    private static long TIME_WINDOW = 60000; // 1 minute in milliseconds
    private static int MAX_REQUESTS = 10; // Max requests per minute

    public AdaptiveRateLimiting() {
    }

    public AdaptiveRateLimiting(long timeWindow, int maxRequests) {
        TIME_WINDOW = timeWindow;
        MAX_REQUESTS = maxRequests;
    }

    @Override
    public boolean shouldLimitRequest(User user) {
        long userId = user.getId();
        long currentTime = System.currentTimeMillis();

        lastAccessTime.putIfAbsent(userId, new AtomicLong(currentTime));
        requestCount.putIfAbsent(userId, new LongAdder());

        long elapsedTime = currentTime - lastAccessTime.get(userId).get();

        if (elapsedTime > TIME_WINDOW) {
            lastAccessTime.get(userId).set(currentTime);
            requestCount.get(userId).reset();
            return false;
        } else {
            return requestCount.get(userId).intValue() >= MAX_REQUESTS;
        }
    }

    @Override
    public boolean tryConsume(User user) {
        if (shouldLimitRequest(user)) {
            try {
                log.warn("Rate limit exceeded for user {}", user.getId());
            } catch (Exception e) {
                log.error("Logging failed: ", e);
            }
            return false;
        } else {
            requestCount.get(user.getId()).increment();
            return true;
        }
    }

    public void reset() {
        lastAccessTime.clear();
        requestCount.clear();
    }

    public void setMaxRequests(int maxRequests) {
        MAX_REQUESTS = maxRequests;
    }

    public void setTimeWindow(long timeWindow) {
        TIME_WINDOW = timeWindow;
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.ADAPTIVE;
    }
}
