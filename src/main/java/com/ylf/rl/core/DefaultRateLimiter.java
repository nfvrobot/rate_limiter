package com.ylf.rl.core;

import com.ylf.rl.config.RateLimiterConfig;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;

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
    public RateLimiterConfig getConfig() {
        return this.rateLimiterConfig;
    }

    @Override
    public void clearState() {

    }
}
