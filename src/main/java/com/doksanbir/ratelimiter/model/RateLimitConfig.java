package com.doksanbir.ratelimiter.model;


import lombok.Data;

import javax.persistence.*;


@Entity
@Data
public class RateLimitConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int maxRequest;
    private long timeWindowInSeconds;

    @Enumerated(EnumType.STRING)
    private AlgorithmType algorithmType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

