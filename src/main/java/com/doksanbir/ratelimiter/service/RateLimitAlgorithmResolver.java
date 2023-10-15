package com.doksanbir.ratelimiter.service;

import com.doksanbir.ratelimiter.model.AlgorithmType;
import com.doksanbir.ratelimiter.config.RateLimitAlgorithm;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RateLimitAlgorithmResolver {

    private final Map<AlgorithmType, RateLimitAlgorithm> algorithms;

    public RateLimitAlgorithmResolver(List<RateLimitAlgorithm> algorithmImplementations) {
        this.algorithms = algorithmImplementations.stream()
                .collect(Collectors.toMap(RateLimitAlgorithm::getAlgorithmType, Function.identity()));
    }

    public RateLimitAlgorithm getAlgorithm(AlgorithmType type) {
        return Optional.ofNullable(algorithms.get(type))
                .orElseThrow(() -> new IllegalArgumentException("Invalid algorithm type"));
    }
}
