package com.ylf.rl.core.config.slw;

import com.ylf.rl.core.RateLimiterType;
import com.ylf.rl.core.config.LimitMode;
import com.ylf.rl.core.config.RateLimiterConfig;

import java.time.Duration;

public record SlidingWindowRateLimiterConfig(String name, LimitMode mode, long limit, Duration interval)
        implements RateLimiterConfig {
    @Override
    public RateLimiterType type() {
        return RateLimiterType.SLIDING_WINDOW;
    }
}
