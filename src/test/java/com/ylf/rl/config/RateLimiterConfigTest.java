package com.ylf.rl.config;

import com.ylf.rl.core.RateLimiterType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RateLimiterConfigTest {
    Duration interval = Duration.ofMinutes(1);

    @Test
    void shouldThrowIllegalArgumentExceptionWhenTypeIsNull() {
        assertThatThrownBy(() -> new RateLimiterConfig(null, "test", 10, interval))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid type");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  "})
    void shouldThrowIllegalArgumentExceptionWhenNameIsInvalid(String name) {
        assertThatThrownBy(() -> new RateLimiterConfig(RateLimiterType.DEFAULT, name, 10, interval))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Name must be not null and not blank");
    }
    
    @ParameterizedTest
    @ValueSource(ints = {0, -5})
    void shouldThrowIllegalArgumentExceptionWhenTokensAreInvalid(int tokens) {
        assertThatThrownBy(() -> new RateLimiterConfig(RateLimiterType.DEFAULT, "test", tokens, interval))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Tokens must be greater than 0");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenIntervalIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new RateLimiterConfig(RateLimiterType.DEFAULT, "test", 10, null));
        assertEquals("Interval must be greater than 0", exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenIntervalIsNegative() {
        interval = Duration.ofMinutes(-1);
        assertThatThrownBy(() -> new RateLimiterConfig(RateLimiterType.DEFAULT, "test", -5, interval))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Tokens must be greater than 0");
    }

    @Test
    void shouldCreateRateLimiterConfigWithValidParameters() {
        var config = assertDoesNotThrow(
                () -> new RateLimiterConfig(RateLimiterType.DEFAULT, "test", 10, Duration.ofMinutes(2)));
        assertEquals(RateLimiterType.DEFAULT, config.type());
        assertEquals("test", config.name());
        assertEquals(10, config.tokens());
        assertEquals(Duration.ofMinutes(2), config.interval());
    }
}