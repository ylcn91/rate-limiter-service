package com.doksanbir.ratelimiter.validator;

import com.doksanbir.ratelimiter.annotation.RateLimited;
import com.doksanbir.ratelimiter.exception.InvalidAlgorithmConfigurationException;
import com.doksanbir.ratelimiter.model.AlgorithmType;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlgorithmConfigValidator {

    public void validate(RateLimited rateLimited) throws InvalidAlgorithmConfigurationException {
        AlgorithmType type = rateLimited.algorithmType();

        switch (type) {
            case TOKEN_BUCKET -> validateTokenBucket(rateLimited);
            case FIXED_WINDOW -> validateFixedWindow(rateLimited);
            case LEAKY_BUCKET -> validateLeakyBucket(rateLimited);
            case PRIORITY_QUEUE -> validatePriorityQueue(rateLimited);
            case SLIDING_WINDOW -> validateSlidingWindow(rateLimited);
            case ADAPTIVE -> validateAdaptive(rateLimited);
            default -> throw new InvalidAlgorithmConfigurationException("Unknown algorithm type");
        }
    }

    private void validateTokenBucket(RateLimited rateLimited) throws InvalidAlgorithmConfigurationException {
        if (rateLimited.tokensPerSecond() <= 0 || rateLimited.tokenBucketSize() <= 0) {
            throw new InvalidAlgorithmConfigurationException("Invalid Token Bucket parameters");
        }
    }

    private void validateFixedWindow(RateLimited rateLimited) throws InvalidAlgorithmConfigurationException {
        if (rateLimited.fixedWindowInMillis() <= 0 || rateLimited.fixedWindowMaxRequests() <= 0) {
            throw new InvalidAlgorithmConfigurationException("Invalid Fixed Window parameters");
        }
    }

    private void validateLeakyBucket(RateLimited rateLimited) throws InvalidAlgorithmConfigurationException {
        if (rateLimited.leakInterval() <= 0) {
            throw new InvalidAlgorithmConfigurationException("Invalid Leaky Bucket parameters");
        }
    }

    private void validatePriorityQueue(RateLimited rateLimited) throws InvalidAlgorithmConfigurationException {
        if (rateLimited.maxQueueSize() <= 0) {
            throw new InvalidAlgorithmConfigurationException("Invalid Priority Queue parameters");
        }
    }

    private void validateSlidingWindow(RateLimited rateLimited) throws InvalidAlgorithmConfigurationException {
        if (rateLimited.slidingWindowInMillis() <= 0 || rateLimited.slidingWindowMaxRequests() <= 0) {
            throw new InvalidAlgorithmConfigurationException("Invalid Sliding Window parameters");
        }
    }

    private void validateAdaptive(RateLimited rateLimited) throws InvalidAlgorithmConfigurationException {
        if (rateLimited.adaptiveInitialTokensPerSecond() <= 0 || rateLimited.adaptiveBucketSize() <= 0) {
            throw new InvalidAlgorithmConfigurationException("Invalid Adaptive parameters");
        }
    }
}

