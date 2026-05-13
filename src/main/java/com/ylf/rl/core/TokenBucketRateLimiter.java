package com.ylf.rl.core;

import com.ylf.rl.core.config.tkb.TokenBucketRateLimiterConfig;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Thread-safe Token Bucket rate limiter implementation.
 * <p>
 * This implementation supports both GLOBAL and UNIQUE modes.
 * Each bucket maintains a token count and refills tokens based on elapsed time.
 * <p>
 * The algorithm uses a lock-free CAS-based state update per bucket.
 * <p>
 * In UNIQUE mode, per-user buckets are periodically evicted by a scheduled cleanup task
 * once they have been idle for {@link #IDLE_TTL}.
 */
public class TokenBucketRateLimiter implements RateLimiter {

    private static final Duration IDLE_TTL = Duration.ofMinutes(10);
    private static final Duration CLEANUP_INTERVAL = Duration.ofMinutes(1);

    private final TokenBucketRateLimiterConfig config;
    private final AtomicReference<State> state;
    private final ConcurrentHashMap<String, AtomicReference<State>> userStates;
    private final ScheduledExecutorService cleanupExecutor;

    protected TokenBucketRateLimiter(TokenBucketRateLimiterConfig config, long capacity) {
        this.config = config;
        this.state = new AtomicReference<>(new State(capacity, System.nanoTime(), System.nanoTime()));
        this.userStates = new ConcurrentHashMap<>(16);
        this.cleanupExecutor = startCleanupScheduler();
    }

    /**
     * Attempts to acquire a token for the given identifier.
     *
     * @param unqId unique caller identifier (used only in UNIQUE mode)
     * @return true if the request is allowed, false otherwise
     */
    @Override
    public boolean tryAcquire(final String unqId) {
        return tryConsumeToken(currentStateRef(unqId));
    }

    private AtomicReference<State> currentStateRef(final String unqId) {
        return switch (config.mode()) {
            case UNIQUE -> userStates.computeIfAbsent(unqId, _ -> new AtomicReference<>(initialState()));
            case GLOBAL -> state;
        };
    }

    private boolean tryConsumeToken(final AtomicReference<State> stateRef) {
        while (true) {
            final var currentState = stateRef.get();
            final var now = System.nanoTime();
            final var sinceLastRefill = Math.max(0L, now - currentState.lastRefillTime());
            final var refilledTokens = currentState.tokens() + sinceLastRefill * config.refillTokensPerNano();
            final var availableTokens = Math.min(refilledTokens, config.capacity());

            if (availableTokens < 1.0) {
                updateAccessTime(stateRef, currentState, now);
                return false;
            }

            final var newState = new State(availableTokens - 1.0, now, now);
            if (stateRef.compareAndSet(currentState, newState)) {
                return true;
            }
        }
    }

    private void updateAccessTime(final AtomicReference<State> stateRef, final State snapshot, final long now) {
        final var bumped = new State(snapshot.tokens(), snapshot.lastRefillTime(), now);
        stateRef.compareAndSet(snapshot, bumped);
    }

    private State initialState() {
        final var now = System.nanoTime();
        return new State(config.capacity(), now, now);
    }

    private ScheduledExecutorService startCleanupScheduler() {
        final var executor = Executors.newSingleThreadScheduledExecutor(runnable -> {
            final var thread = new Thread(runnable, "token-bucket-cleanup");
            thread.setDaemon(true);
            return thread;
        });
        executor.scheduleAtFixedRate(
                this::evictStaleEntries,
                CLEANUP_INTERVAL.toNanos(),
                CLEANUP_INTERVAL.toNanos(),
                TimeUnit.NANOSECONDS
        );
        return executor;
    }

    private void evictStaleEntries() {
        try {
            final var ttlNanos = IDLE_TTL.toNanos();
            final var now = System.nanoTime();

            userStates.forEach((key, ref) -> {
                final var snapshot = ref.get();
                if (snapshot == null) {
                    return;
                }
                if (now - snapshot.lastAccessTime() < ttlNanos) {
                    return;
                }
                final var latest = ref.get();
                if (now - latest.lastAccessTime() >= ttlNanos) {
                    userStates.remove(key, ref);
                }
            });
        } catch (Throwable _) {
        }
    }

    /**
     * Resets the rate limiter state to its initial capacity.
     * <p>
     * In GLOBAL mode resets global bucket.
     * In UNIQUE mode resets only global state; user-specific states are not cleared.
     */
    @Override
    public void clearState() {
        this.state.set(initialState());
        this.userStates.clear();
    }

    @Override
    public void close() {
        cleanupExecutor.shutdownNow();
    }

    private record State(double tokens, long lastRefillTime, long lastAccessTime) {
    }
}
