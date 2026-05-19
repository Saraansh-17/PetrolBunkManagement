package com.petrolpump.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Per-IP rate limiting (requests per minute) backed by Redis when enabled.
 */
@ConfigurationProperties(prefix = "gateway.rate-limit")
public class GatewayRateLimitProperties {

    private boolean enabled = true;

    /**
     * Maximum requests per client IP per rolling minute window.
     */
    private int requestsPerMinute = 100;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getRequestsPerMinute() {
        return requestsPerMinute;
    }

    public void setRequestsPerMinute(int requestsPerMinute) {
        this.requestsPerMinute = requestsPerMinute;
    }
}
