package com.ylf.rl.core;

import com.ylf.rl.core.config.cncr.ConcurrencyRateLImiterConfig;

public class ConcurrencyRateLimiter implements RateLimiter {
    private final ConcurrencyRateLImiterConfig config;

    public ConcurrencyRateLimiter(ConcurrencyRateLImiterConfig config) {
        this.config = config;
    }

    @Override
    public boolean tryAcquire(String unqId) {
        return false;
    }

    @Override
    public void clearState() {

    }

    @Override
    public void close() {

    }
}
