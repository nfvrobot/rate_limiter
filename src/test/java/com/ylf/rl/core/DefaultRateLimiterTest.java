package com.ylf.rl.core;

import com.ylf.rl.config.RateLimiterConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

class DefaultRateLimiterTest {

    @Test
    void shouldLimitCallsWhenExceedingTokens() {
        var config = new RateLimiterConfig(RateLimiterType.DEFAULT, "testLimiter", 3, Duration.ofMinutes(1));
        var rateLimiter = new DefaultRateLimiter("testLimiter", config, 10);

        rateLimiter.isCallLimited("user1");
        rateLimiter.isCallLimited("user1");
        rateLimiter.isCallLimited("user1");

        assertThat(rateLimiter.isCallLimited("user1")).isTrue();
    }

    @Test
    void shouldReleaseLockAfterDuration() {
        var config = new RateLimiterConfig(RateLimiterType.DEFAULT, "testLimiter", 1, Duration.ofSeconds(1));
        var rateLimiter = new DefaultRateLimiter("testLimiter", config, 10);

        assertThat(rateLimiter.isCallLimited("user1")).isFalse();
        assertThat(rateLimiter.isCallLimited("user1")).isTrue();

        await()
                .atMost(config.interval().plusMillis(200))
                .untilAsserted(() -> assertThat(rateLimiter.isCallLimited("user1")).isFalse());

    }

    @ParameterizedTest
    @CsvSource({
            "1, false",
            "2, false",
            "3, false"
    })
    void shouldNotLimitCallsWhenWithinTokens(int callCount, boolean expectedLimited) {
        var config = new RateLimiterConfig(RateLimiterType.DEFAULT, "testLimiter", 3, Duration.ofMinutes(1));
        var rateLimiter = new DefaultRateLimiter("testLimiter", config, 10);

        boolean isLimited = false;
        for (int i = 0; i < callCount; i++) {
            isLimited = rateLimiter.isCallLimited("user2");
        }

        assertThat(isLimited).isEqualTo(expectedLimited);
    }
}