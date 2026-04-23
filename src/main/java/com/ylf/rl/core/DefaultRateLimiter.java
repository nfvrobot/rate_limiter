package com.ylf.rl.core;

import com.ylf.rl.config.RateLimiterConfig;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DefaultRateLimiter is the default implementation of the {@link RateLimiter} interface.
 * It provides a mechanism to limit calls based on a sliding window log algorithm.
 * The rate limiter restricts the number of calls that can be made within a specific time interval,
 * by maintaining a log of past call timestamps per unique caller ID and evicting
 * entries that fall outside the current window on each invocation.
 */
public class DefaultRateLimiter implements RateLimiter {

    private final String name;
    private final RateLimiterConfig rateLimiterConfig;
    private final ConcurrentHashMap<String, Deque<LocalDateTime>> callAudit;

    protected DefaultRateLimiter(String name, RateLimiterConfig rateLimiterConfig, int capacity) {
        this.name = name;
        this.rateLimiterConfig = rateLimiterConfig;
        this.callAudit = new ConcurrentHashMap<>(capacity);
    }

    public String getName() {
        return name;
    }

    public RateLimiterConfig getRateLimiterConfig() {
        return rateLimiterConfig;
    }

    /**
     * Checks if a call is limited based on the configured rate limit.
     * @param unqId Unique identifier for the caller.
     * @return True if the call is limited, false otherwise.
     */
    @Override
    public boolean isCallLimited(final String unqId) {
        final var now = LocalDateTime.now();
        final var threshold = now.minus(rateLimiterConfig.interval());
        final var calls = callAudit.computeIfAbsent(unqId, _ -> new ArrayDeque<>());

        calls.removeIf(callTime -> callTime.isBefore(threshold));
        if (calls.size() >= rateLimiterConfig.tokens()) {
            return true;
        }

        calls.add(now);
        return false;
    }

    @Override
    public void clearState() {

    }
}
