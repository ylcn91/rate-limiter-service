package com.doksanbir.ratelimiter.controller;

import com.doksanbir.ratelimiter.config.RateLimitAlgorithm;
import com.doksanbir.ratelimiter.model.AlgorithmType;
import com.doksanbir.ratelimiter.model.User;
import com.doksanbir.ratelimiter.security.AuthenticationFacade;
import com.doksanbir.ratelimiter.service.RateLimitAlgorithmResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DynamicAlgorithmController {

    private final RateLimitAlgorithmResolver rateLimitAlgorithmResolver;
    private final AuthenticationFacade authenticationFacade;

    @RequestMapping(value = "/dynamic/{algorithmType}", method = RequestMethod.GET)
    public ResponseEntity<String> handleDynamicAlgorithm(@PathVariable AlgorithmType algorithmType) {

        RateLimitAlgorithm algorithm = rateLimitAlgorithmResolver.getAlgorithm(algorithmType);
        User user = authenticationFacade.getAuthenticatedUser();

        if (algorithm.canConsume(user)) {
            return new ResponseEntity<>("Request processed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Rate limit exceeded", HttpStatus.TOO_MANY_REQUESTS);
        }
    }
}

