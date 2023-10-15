package com.doksanbir.ratelimiter.repository;

import com.doksanbir.ratelimiter.model.RateLimitConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RateLimitConfigRepository extends JpaRepository<RateLimitConfig, Long> {
    Optional<RateLimitConfig> findByUserId(Long userId);
}

