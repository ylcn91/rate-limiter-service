package com.doksanbir.ratelimiter.model;

public enum AlgorithmType {
    TOKEN_BUCKET,
    LEAKY_BUCKET,
    FIXED_WINDOW,
    SLIDING_WINDOW,
    ADAPTIVE,
    PRIORITY_QUEUE
}
