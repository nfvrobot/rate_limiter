package com.ylf.rl.core;

import com.ylf.rl.core.config.LimitMode;
import com.ylf.rl.core.config.slw.SlidingWindowRateLimiterConfig;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rate limiter implementation based on the sliding window log algorithm.
 * <p>
 * It limits the number of calls within a configured time interval by tracking
 * timestamps of incoming requests.
 * <p>
 * Depending on configuration, limits can be applied per unique caller ID
 * or globally across all callers.
 */
public class SlidingWindowRateLimiter implements RateLimiter {
    private static final String GLOBAL_KEY = "__GLOBAL__";

    private final SlidingWindowRateLimiterConfig config;
    private final ConcurrentHashMap<String, ArrayDeque<Instant>> callAudit;

    protected SlidingWindowRateLimiter(SlidingWindowRateLimiterConfig config, long capacity) {
        this.config = config;
        this.callAudit = new ConcurrentHashMap<>(Math.toIntExact(capacity));
    }

    public String getName() {
        return this.config.name();
    }

    /**
     * Evaluates whether a request should be rate-limited and updates internal state.
     * <p>
     * This method applies the sliding window algorithm, removing expired entries
     * and recording the current request timestamp if it is allowed.
     *
     * @param unqId unique identifier of the caller
     * @return true if the request exceeds the rate limit and should be blocked,
     *         false if the request is allowed
     */
    @Override
    public boolean tryAcquire(final String unqId) {
        final var key = config.mode() == LimitMode.UNIQUE ? unqId : GLOBAL_KEY;

        final var now = Instant.now();
        final var threshold = now.minus(config.interval());
        final var calls = callAudit.computeIfAbsent(key, _ -> new ArrayDeque<>());

        synchronized (calls) {
            while (!calls.isEmpty()) {
                final var oldest = calls.peekFirst();
                if (oldest.isBefore(threshold)) {
                    calls.pollFirst();
                    continue;
                }
                break;
            }
            if (calls.size() >= config.limit()) {
                return false;
            }
            calls.addLast(now);
            return true;
        }
    }

    @Override
    public void clearState() {
        callAudit.clear();
    }

    @Override
    public void close() {

    }
}
