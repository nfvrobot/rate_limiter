package com.ylf.rl.core.config.tkb;

import com.ylf.rl.core.RateLimiterType;
import com.ylf.rl.core.config.LimitMode;
import com.ylf.rl.core.config.RateLimiterConfig;

import java.time.Duration;

public record TokenBucketRateLimiterConfig(String name, LimitMode mode, long capacity, double refillTokensPerNano,
                                           Duration idleTtl, Duration cleanupInterval)
        implements RateLimiterConfig {
    @Override
    public RateLimiterType type() {
        return RateLimiterType.TOKEN_BUCKET;
    }
}
