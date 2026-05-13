package com.ylf.rl.core;

public enum RateLimiterType {

    SLIDING_WINDOW,
    FIXED_WINDOW,
    TOKEN_BUCKET,
    CONCURRENCY,
    LEAKY_BUCKET
}
