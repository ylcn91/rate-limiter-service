package com.doksanbir.ratelimiter.algorithm;

import com.doksanbir.ratelimiter.config.RateLimitAlgorithm;
import com.doksanbir.ratelimiter.model.AlgorithmType;
import com.doksanbir.ratelimiter.model.User;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class LeakyBucketAlgorithm implements RateLimitAlgorithm {

    private final ConcurrentHashMap<Long, Long> lastRequestTime = new ConcurrentHashMap<>();
    private final long leakInterval;
    private final AtomicBoolean shutdown = new AtomicBoolean(false);

    public LeakyBucketAlgorithm(long leakInterval, ScheduledExecutorService scheduler) {
        this.leakInterval = leakInterval;

        // Schedule a task to refill the buckets every leakInterval milliseconds.
        scheduler.scheduleAtFixedRate(() -> {
            if (!shutdown.get()) {
                lastRequestTime.replaceAll((userId, previousTime) -> {
                    if (previousTime == null || System.currentTimeMillis() - previousTime >= leakInterval) {
                        return System.currentTimeMillis();
                    } else {
                        return previousTime;
                    }
                });
            }
        }, leakInterval, leakInterval, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean shouldLimitRequest(User user) {
        return !tryConsume(user);
    }

    @Override
    public boolean tryConsume(User user) {
        long currentTime = System.currentTimeMillis();

        Long previousTime = lastRequestTime.get(user.getId());
        if (previousTime == null || currentTime - previousTime >= leakInterval) {
            return true;
        } else {
            log.warn("Rate limit exceeded for user: {}", user.getUsername());
            return false;
        }
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.LEAKY_BUCKET;
    }

    public void shutdown() {
        shutdown.set(true);
    }
}
