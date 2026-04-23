package com.ylf.rl;

import com.ylf.rl.config.RateLimiterConfig;
import com.ylf.rl.core.RateLimiterType;

import java.time.Duration;

public class MainApp {
    static void main() {
        var config = new RateLimiterConfig(RateLimiterType.DEFAULT, "core", 2, Duration.ofSeconds(1));
        System.out.println("hi ho");
    }
}
