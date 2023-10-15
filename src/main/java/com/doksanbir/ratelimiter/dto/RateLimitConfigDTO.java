package com.doksanbir.ratelimiter.dto;


import com.doksanbir.ratelimiter.model.AlgorithmType;
import lombok.Data;

@Data
public class RateLimitConfigDTO {
    private Long id;
    private int maxRequest;
    private long timeWindowInSeconds;
    private AlgorithmType algorithmType;
    private Long userId; // The ID of the user to whom this config belongs
}

