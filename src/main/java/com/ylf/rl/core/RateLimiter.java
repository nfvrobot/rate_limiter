package com.ylf.rl.core;

import com.ylf.rl.config.RateLimiterConfig;

import java.time.Duration;

/**
 * RateLimiter interface defines the contract for rate-limiting operations.
 */
public interface RateLimiter {

    int DEFAULT_RATE_LIMITER_TOKENS = 10;
    RateLimiterType TYPE = RateLimiterType.DEFAULT;
    String DEFAULT_RATE_LIMITER_NAME = "default";
    Duration DEFAULT_RATE_LIMITER_INTERVAL = Duration.ofMinutes(2);
    RateLimiterConfig DEFAULT_RATE_LIMITER_CONFIG = new RateLimiterConfig(TYPE, DEFAULT_RATE_LIMITER_NAME,
            DEFAULT_RATE_LIMITER_TOKENS, DEFAULT_RATE_LIMITER_INTERVAL);

    /**
     * Factory method to create a new RateLimiter instance. If no name is provided, a default name will be used.
     * If no configuration is provided, a default configuration will be used. The default configuration is:
     * name = "default", tokens = 10, 'interval' = 2 minutes.
     *
     * @param name              the name of the rate limiter
     * @param rateLimiterConfig the configuration for the rate limiter
     * @return a new {@link RateLimiter} instance backed by {@link DefaultRateLimiter}
     */
    static RateLimiter of(String name, RateLimiterConfig rateLimiterConfig) {
        if (name == null || name.isBlank()) {
            name = DEFAULT_RATE_LIMITER_NAME;
        }
        if (rateLimiterConfig == null) {
            rateLimiterConfig = DEFAULT_RATE_LIMITER_CONFIG;
        }
        return RateLimiterRegistry.getInstance().getOrCreate(name, new DefaultRateLimiter(name, rateLimiterConfig,
                rateLimiterConfig.tokens() + 1));
    }

    boolean isCallLimited(String unqId);

    void clearState();


}
