package com.ylf.rl.core;

import com.ylf.rl.core.config.LimitMode;
import com.ylf.rl.core.config.RateLimiterConfig;
import com.ylf.rl.core.config.cncr.ConcurrencyRateLImiterConfig;
import com.ylf.rl.core.config.slw.SlidingWindowRateLimiterConfig;
import com.ylf.rl.core.config.tkb.TokenBucketRateLimiterConfig;

import java.time.Duration;

public class RateLimiterFactory {
    private final RateLimiterRegistry registry = RateLimiterRegistry.getInstance();

    /**
     * Returns a RateLimiter instance for the given name and configuration.
     * <p>
     * If the name is null or blank, a default name is used.
     * If the configuration is null, a default configuration is applied.
     * <p>
     * Instances are managed by {@link RateLimiterRegistry} and may be reused.
     * <p>
     * @param name              optional identifier of the rate limiter
     * @param rateLimiterConfig configuration describing rate limiting policy
     * @return a RateLimiter instance
     */
    public RateLimiter of(String name, RateLimiterConfig rateLimiterConfig) {
        if (name == null || name.isBlank()) {
            name = Defaults.NAME;
        }
        if (rateLimiterConfig == null) {
            final var defaultConfig = defaultConfig();
            return registry.getOrCreate(name,
                    () -> new SlidingWindowRateLimiter(defaultConfig, Defaults.LIMIT + 1L));
        }
        try (var rateLimiter = createExplicitType(rateLimiterConfig)) {
            return registry.getOrCreate(name, () -> rateLimiter);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid rate limiter configuration: " + rateLimiterConfig, e);
        }
    }

    private static RateLimiter createExplicitType(RateLimiterConfig config) {
        final var type = config.type();
        return switch (type) {
            case SLIDING_WINDOW -> {
                final var typedConfig = (SlidingWindowRateLimiterConfig) config;
                yield new SlidingWindowRateLimiter(typedConfig, typedConfig.limit());
            }
            case TOKEN_BUCKET -> {
                final var typedConfig = (TokenBucketRateLimiterConfig) config;
                yield new TokenBucketRateLimiter(typedConfig, typedConfig.capacity());
            }
            case CONCURRENCY -> {
                final var typedConfig = (ConcurrencyRateLImiterConfig) config;
                yield new ConcurrencyRateLimiter(typedConfig);
            }
            default -> throw new IllegalArgumentException("Unsupported rate limiter type: " + type);
        };
    }

    private static SlidingWindowRateLimiterConfig defaultConfig() {
        return new SlidingWindowRateLimiterConfig(
                Defaults.NAME,
                Defaults.MODE,
                Defaults.LIMIT,
                Defaults.INTERVAL
        );
    }

    private static final class Defaults {
        static final int LIMIT = 10;
        static final LimitMode MODE = LimitMode.GLOBAL;
        static final String NAME = "default";
        static final Duration INTERVAL = Duration.ofMinutes(2);
    }
}
