package com.ylf.rl.core;

import com.ylf.rl.core.config.RateLimiterConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

class SlidingWindowRateLimiterTest {

    @Test
    void shouldLimitCallsWhenExceedingTokens() {
        var config = new RateLimiterConfig();
        var rateLimiter = new SlidingWindowRateLimiter(config, 10);

        rateLimiter.tryAcquire("user1");
        rateLimiter.tryAcquire("user1");
        rateLimiter.tryAcquire("user1");

        assertThat(rateLimiter.tryAcquire("user1")).isTrue();
    }

    @Test
    void shouldReleaseLockAfterDuration() {
        var config = new RateLimiterConfig();
        var rateLimiter = new SlidingWindowRateLimiter(config, 10);

        assertThat(rateLimiter.tryAcquire("user1")).isFalse();
        assertThat(rateLimiter.tryAcquire("user1")).isTrue();

        await()
                .atMost(config.interval().plusMillis(200))
                .untilAsserted(() -> assertThat(rateLimiter.tryAcquire("user1")).isFalse());

    }

    @ParameterizedTest
    @CsvSource({
            "1, false",
            "2, false",
            "3, false"
    })
    void shouldNotLimitCallsWhenWithinTokens(int callCount, boolean expectedLimited) {
        var config = new RateLimiterConfig();
        var rateLimiter = new SlidingWindowRateLimiter(config, 10);

        boolean isLimited = false;
        for (int i = 0; i < callCount; i++) {
            isLimited = rateLimiter.tryAcquire("user2");
        }

        assertThat(isLimited).isEqualTo(expectedLimited);
    }
}