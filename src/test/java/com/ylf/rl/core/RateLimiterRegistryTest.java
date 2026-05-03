package com.ylf.rl.core;

import com.ylf.rl.config.RateLimiterConfig;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class RateLimiterRegistryTest {

    @Test
    void shouldReturnSingletonInstanceOfRateLimiterRegistry() {
        var instance1 = RateLimiterRegistry.getInstance();
        var instance2 = RateLimiterRegistry.getInstance();

        assertThat(instance1).isNotNull();
        assertThat(instance2).isNotNull();
        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    void shouldCreateAndReturnNewRateLimiter() {
        var registry = RateLimiterRegistry.getInstance();
        var config = new RateLimiterConfig(RateLimiterType.DEFAULT, "testLimiter", 5, Duration.ofSeconds(60));
        var rateLimiter = new DefaultRateLimiter("testLimiter", config, config.tokens() + 1);

        var createdLimiter = registry.getOrCreate("testLimiter", rateLimiter);

        assertThat(createdLimiter)
                .isNotNull()
                .isSameAs(rateLimiter);
    }

    @Test
    void shouldReturnExistingRateLimiterIfAlreadyCreated() {
        var registry = RateLimiterRegistry.getInstance();
        var config = new RateLimiterConfig(RateLimiterType.DEFAULT, "existingLimiter", 10, Duration.ofMinutes(1));
        var rateLimiter = new DefaultRateLimiter("existingLimiter", config, config.tokens());
        registry.getOrCreate("existingLimiter", rateLimiter);


        var retrievedLimiter = registry.getOrCreate("existingLimiter", new DefaultRateLimiter(
                "existingLimiter", config, config.tokens() + 1));

        assertThat(retrievedLimiter)
                .isNotNull()
                .isSameAs(rateLimiter);
    }
}