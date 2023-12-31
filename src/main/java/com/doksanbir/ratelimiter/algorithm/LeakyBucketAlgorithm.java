package com.doksanbir.ratelimiter.algorithm;

import com.doksanbir.ratelimiter.config.RateLimitAlgorithm;
import com.doksanbir.ratelimiter.model.AlgorithmType;
import com.doksanbir.ratelimiter.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class LeakyBucketAlgorithm implements RateLimitAlgorithm {

    private final ConcurrentHashMap<Long, Long> lastRequestTime = new ConcurrentHashMap<>();

    @Value("${leakyBucket.leakInterval}")
    private long leakInterval;
    private final AtomicBoolean shutdown = new AtomicBoolean(false);

    public LeakyBucketAlgorithm(ScheduledExecutorService scheduler) {

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
        return canConsume(user);
    }

    @Override
    public boolean canConsume(User user) {
        long currentTime = System.currentTimeMillis();

        Long previousTime = lastRequestTime.get(user.getId());
        if (previousTime == null || currentTime - previousTime >= leakInterval) {
            return false;
        } else {
            log.warn("Rate limit exceeded for user: {}", user.getUsername());
            return true;
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
