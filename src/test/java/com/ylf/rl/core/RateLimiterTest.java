package com.ylf.rl.core;

import com.ylf.rl.config.RateLimiterConfig;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class RateLimiterTest {

    @Test
    void shouldCreateRateLimiterWithProvidedNameAndConfig() {
        var config = new RateLimiterConfig(RateLimiterType.DEFAULT, "configName", 20, Duration.ofMinutes(1));
        var rateLimiter = RateLimiter.of("rateLimiterOf", config);

        assertThat(rateLimiter).isInstanceOfSatisfying(DefaultRateLimiter.class, rl -> {
            assertThat(rl.getName()).isEqualTo("rateLimiterOf");
            assertThat(rl.getRateLimiterConfig()).isEqualTo(config);
        });
    }

    @Test
    void shouldUseDefaultNameWhenNameIsNull() {
        var config = new RateLimiterConfig(RateLimiterType.DEFAULT, "configName", 20, Duration.ofMinutes(1));
        var rateLimiter = RateLimiter.of(null, config);

        assertThat(rateLimiter).isInstanceOfSatisfying(DefaultRateLimiter.class, rl -> {
            assertThat(rl.getName()).isEqualTo(RateLimiter.DEFAULT_RATE_LIMITER_NAME);
            assertThat(rl.getRateLimiterConfig()).isEqualTo(config);
        });
    }

    @Test
    void shouldUseDefaultConfigurationWhenConfigIsNull() {
        var rateLimiter = RateLimiter.of("rateLimiterWithDefaultConfig", null);

        assertThat(rateLimiter).isInstanceOfSatisfying(DefaultRateLimiter.class, rl -> {
            assertThat(rl.getName()).isEqualTo("rateLimiterWithDefaultConfig");
            assertThat(rl.getRateLimiterConfig()).isEqualTo(RateLimiter.DEFAULT_RATE_LIMITER_CONFIG);
        });
    }
}