package com.doksanbir.ratelimiter.service;

import com.doksanbir.ratelimiter.model.RateLimitConfig;
import com.doksanbir.ratelimiter.model.User;
import com.doksanbir.ratelimiter.repository.RateLimitConfigRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateLimitConfigServiceImpl implements RateLimitConfigService {

    private final RateLimitConfigRepository rateLimitConfigRepository;

    @Override
    public RateLimitConfig saveRateLimitConfig(RateLimitConfig rateLimitConfig) {
        log.info("Saving rate limit config for user ID {}", rateLimitConfig.getUser().getId());
        return rateLimitConfigRepository.save(rateLimitConfig);
    }

    @Override
    public Optional<RateLimitConfig> getRateLimitConfigById(Long id) {
        log.info("Fetching rate limit config by ID {}", id);
        return rateLimitConfigRepository.findById(id);
    }

    @Override
    public List<RateLimitConfig> getAllRateLimitConfigs() {
        log.info("Fetching all rate limit configs");
        return rateLimitConfigRepository.findAll();
    }

    @Override
    public void deleteRateLimitConfig(Long id) {
        log.info("Deleting rate limit config with ID {}", id);
        rateLimitConfigRepository.deleteById(id);
    }

    @Override
    public Optional<RateLimitConfig> getRateLimitConfigByUser(User user) {
        log.info("Fetching rate limit config for user ID {}", user.getId());
        return rateLimitConfigRepository.findByUserId(user.getId());
    }
}
