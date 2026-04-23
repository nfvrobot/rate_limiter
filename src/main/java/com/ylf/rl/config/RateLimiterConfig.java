package com.ylf.rl.config;

import java.time.Duration;

public class Configuration {

    private String key;

    private int limit;

    private Duration period;

    public Configuration(String key, int limit, Duration period) {
        this.key = key;
        this.limit = limit;
        this.period = period;
    }

    public Configuration(int limit, Duration period) {
        this.limit = limit;
        this.period = period;
    }

    public Configuration(Duration period) {
        this.period = period;
    }

    public Configuration() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Duration getPeriod() {
        return period;
    }

    public void setPeriod(Duration period) {
        this.period = period;
    }
}
