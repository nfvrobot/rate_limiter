package com.ylf.rl.config;

import com.ylf.rl.core.RateLimiterType;

import java.time.Duration;

public record RateLimiterConfig(RateLimiterType type, String name, int tokens, Duration interval) {
    public RateLimiterConfig {
        checkType(type);
        checkName(name);
        checkTokens(tokens);
        checkInterval(interval);
    }

    private static void checkType(RateLimiterType type) {
        if (type == null) {
            throw new IllegalArgumentException("Invalid type");
        }
    }

    private static void checkInterval(Duration interval) {
        if (interval == null || interval.isNegative()) {
            throw new IllegalArgumentException("Interval must be greater than 0");
        }
    }

    private static void checkName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must be not null and not blank");
        }
    }

    private static void checkTokens(int tokens) {
        if (tokens <= 0) {
            throw new IllegalArgumentException("Tokens must be greater than 0");
        }
    }
}
