package com.ylf.rl.core;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TokenBucketRateLimiterTest {

    @Test
    void shouldReturnTrueWhenBucketIsEmpty() {
        var rateLimiter = new TokenBucketRateLimiter(1, 0);
        var isLimited = rateLimiter.tryAcquire("testId");

        assertThat(isLimited).isTrue();
    }

    @Test
    void shouldReturnFalseWhenBucketIsFull() {
        var rateLimiter = new TokenBucketRateLimiter(1, 1);
        var isLimited = rateLimiter.tryAcquire("testId");

        assertThat(isLimited).isFalse();
    }

    @Test
    void shouldDecreaseTokensAfterEachCall() {
        var rateLimiter = new TokenBucketRateLimiter(2, 0);

        var firstCallLimited = rateLimiter.tryAcquire("testId");
        assertThat(firstCallLimited).isFalse();

        var secondCallLimited = rateLimiter.tryAcquire("testId");
        assertThat(secondCallLimited).isFalse();

        var thirdCallLimited = rateLimiter.tryAcquire("testId");
        assertThat(thirdCallLimited).isTrue();
    }

    @Test
    void shouldRefillTokensOverTime() {
        var rateLimiter = new TokenBucketRateLimiter(2, 1);
        var isLimitedFirst = rateLimiter.tryAcquire("testId");
        assertThat(isLimitedFirst).isFalse();

        try {
            Thread.sleep(1000); // Simulate time for token refill
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        var isLimitedAfterRefill = rateLimiter.tryAcquire("testId");
        assertThat(isLimitedAfterRefill).isFalse();
    }
}