package com.ylf.rl.core.config.cncr;

import com.ylf.rl.core.RateLimiterType;
import com.ylf.rl.core.config.RateLimiterConfig;

public record ConcurrencyRateLImiterConfig() implements RateLimiterConfig {
    @Override
    public RateLimiterType type() {
        return RateLimiterType.CONCURRENCY;
    }
}
