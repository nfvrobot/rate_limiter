package com.ylf.rl.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Facade for managing RateLimiter instances.
 * Acts as the single entry point for creating, retrieving, and listing rate limiters.
 */
public class RateLimiterRegistry {

    private static final RateLimiterRegistry INSTANCE = new RateLimiterRegistry(16);

    private final ConcurrentHashMap<String, RateLimiter> rateLimiterMap;

    private RateLimiterRegistry(int initialCapacity) {
        this.rateLimiterMap = new ConcurrentHashMap<>(initialCapacity);
    }

    protected static RateLimiterRegistry getInstance() {
        return INSTANCE;
    }

    /**
     * Returns an existing RateLimiter by name, or creates and registers a new one.
     */
    protected RateLimiter getOrCreate(String name, Supplier<RateLimiter> supplier) {
        return rateLimiterMap.computeIfAbsent(name, _ ->  supplier.get());
    }
}
