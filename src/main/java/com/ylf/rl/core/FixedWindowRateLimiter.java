package com.ylf.rl.core;

public class FixedWindowRateLimiter implements RateLimiter {
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
