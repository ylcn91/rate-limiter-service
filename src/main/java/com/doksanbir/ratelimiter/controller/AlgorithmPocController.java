package com.doksanbir.ratelimiter.controller;

import com.doksanbir.ratelimiter.annotation.RateLimited;
import com.doksanbir.ratelimiter.model.AlgorithmType;
import com.doksanbir.ratelimiter.service.RateLimitAlgorithmResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/poc")
@RequiredArgsConstructor
public class AlgorithmPocController {

    private final RateLimitAlgorithmResolver rateLimitAlgorithmResolver;

    @GetMapping("/tokenBucket")
    @RateLimited(algorithmType = AlgorithmType.TOKEN_BUCKET)
    public ResponseEntity<String> tokenBucket() {
        return new ResponseEntity<>("Token Bucket algorithm triggered", HttpStatus.OK);
    }

    @GetMapping("/fixedWindow")
    @RateLimited(algorithmType = AlgorithmType.FIXED_WINDOW)
    public ResponseEntity<String> fixedWindow() {
        return new ResponseEntity<>("Fixed Window algorithm triggered", HttpStatus.OK);
    }

    @GetMapping("/leakyBucket")
    @RateLimited(algorithmType = AlgorithmType.LEAKY_BUCKET)
    public ResponseEntity<String> leakyBucket() {
        return new ResponseEntity<>("Leaky Bucket algorithm triggered", HttpStatus.OK);
    }

    @GetMapping("/priorityQueue")
    @RateLimited(algorithmType = AlgorithmType.PRIORITY_QUEUE)
    public ResponseEntity<String> priorityQueue() {
        return new ResponseEntity<>("Priority Queue algorithm triggered", HttpStatus.OK);
    }

    @GetMapping("/slidingWindow")
    @RateLimited(algorithmType = AlgorithmType.SLIDING_WINDOW)
    public ResponseEntity<String> slidingWindow() {
        return new ResponseEntity<>("Sliding Window algorithm triggered", HttpStatus.OK);
    }

    @GetMapping("/adaptive")
    @RateLimited(algorithmType = AlgorithmType.ADAPTIVE)
    public ResponseEntity<String> adaptive() {
        return new ResponseEntity<>("Adaptive algorithm triggered", HttpStatus.OK);
    }
}

