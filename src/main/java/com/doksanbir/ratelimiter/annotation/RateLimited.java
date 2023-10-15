package com.doksanbir.ratelimiter.annotation;


import com.doksanbir.ratelimiter.model.AlgorithmType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimited {
    AlgorithmType algorithmType() default AlgorithmType.TOKEN_BUCKET;

    // For Token Bucket Algorithm
    long tokensPerSecond() default 10;
    long tokenBucketSize() default 100;

    // For Fixed Window Algorithm
    long fixedWindowInMillis() default 60000;  // 1 minute
    int fixedWindowMaxRequests() default 100;

    // For Leaky Bucket Algorithm
    long leakInterval() default 1000;  // 1 second

    // For Priority Queue Rate Limiting
    int maxQueueSize() default 100;

    // For Sliding Window Algorithm
    long slidingWindowInMillis() default 60000;  // 1 minute
    int slidingWindowMaxRequests() default 100;

    // For Adaptive Rate Limiting
    long adaptiveInitialTokensPerSecond() default 10;
    long adaptiveBucketSize() default 100;
}

