package com.ylf.rl.core;

/**
 * Contract for a rate limiter that controls request throughput based on a configured policy.
 * <p>
 * Implementations define how requests are tracked and limited over time,
 * typically using time-based or token-based algorithms.
 * <p>
 * A rate limiter is expected to be thread-safe.
 */
public interface RateLimiter extends AutoCloseable {

    /**
     * Attempts to acquire permission for a request.
     *
     * @param unqId identifier of the caller
     * @return true if the request is allowed, false if it exceeds the rate limit
     */
    boolean tryAcquire(String unqId);

    /**
     * Clears all internal tracking state for this rate limiter.
     * This effectively resets the rate-limiting counters/windows.
     */
    void clearState();

    /**
     * Releases any resources held by this rate limiter
     * (background schedulers, executors, listeners, etc.).
     * <p>
     * Must be idempotent. Calling {@link #tryAcquire(String)} after {@code close()}
     * is allowed but may not benefit from background maintenance (e.g. eviction).
     */
    @Override
    void close();
}
