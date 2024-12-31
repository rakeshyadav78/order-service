package com.tgd.order.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class OrderMsCircuitbreakerConfig {

    @Bean
    public CircuitBreaker custMsCircuitbreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(10)
                .minimumNumberOfCalls(5)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .permittedNumberOfCallsInHalfOpenState(3)
                .waitDurationInOpenState(Duration.ofSeconds(30))
                .failureRateThreshold(50)
                .build();

        return circuitBreakerRegistry.circuitBreaker("custms", circuitBreakerConfig);
    }
}
