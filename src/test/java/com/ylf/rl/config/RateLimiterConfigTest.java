package com.ylf.rl.config;

import com.ylf.rl.core.RateLimiterType;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RateLimiterConfigTest {

    @Test
    void shouldThrowIllegalArgumentExceptionWhenTypeIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new RateLimiterConfig(null, "test", 10, Duration.ofMinutes(1)));
        assertEquals("Invalid type", exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenNameIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new RateLimiterConfig(RateLimiterType.DEFAULT, null, 10, Duration.ofMinutes(1)));
        assertEquals("Name must be not null and not blank", exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenNameIsBlank() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new RateLimiterConfig(RateLimiterType.DEFAULT, "  ", 10, Duration.ofMinutes(1)));
        assertEquals("Name must be not null and not blank", exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenTokensIsZero() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new RateLimiterConfig(RateLimiterType.DEFAULT, "test", 0, Duration.ofMinutes(1)));
        assertEquals("Tokens must be greater than 0", exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenTokensIsNegative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new RateLimiterConfig(RateLimiterType.DEFAULT, "test", -5, Duration.ofMinutes(1)));
        assertEquals("Tokens must be greater than 0", exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenIntervalIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new RateLimiterConfig(RateLimiterType.DEFAULT, "test", 10, null));
        assertEquals("Interval must be greater than 0", exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenIntervalIsNegative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new RateLimiterConfig(RateLimiterType.DEFAULT, "test", 10, Duration.ofMinutes(-1)));
        assertEquals("Interval must be greater than 0", exception.getMessage());
    }

    @Test
    void shouldCreateRateLimiterConfigWithValidParameters() {
        RateLimiterConfig config = assertDoesNotThrow(
                () -> new RateLimiterConfig(RateLimiterType.DEFAULT, "test", 10, Duration.ofMinutes(2)));
        assertEquals(RateLimiterType.DEFAULT, config.type());
        assertEquals("test", config.name());
        assertEquals(10, config.tokens());
        assertEquals(Duration.ofMinutes(2), config.interval());
    }
}