package com.ylf.rl.core;

public class FixedWindowRateLimiter implements RateLimiter {
    @Override
    public boolean isCallLimited(String unqId) {
        return false;
    }

    @Override
    public void clearState() {

    }
}
