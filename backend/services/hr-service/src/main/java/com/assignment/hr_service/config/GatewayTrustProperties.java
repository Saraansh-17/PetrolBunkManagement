package com.assignment.hr_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Validates that requests proxied through the API Gateway carry the shared internal token header.
 */
@ConfigurationProperties(prefix = "gateway.trust")
public class GatewayTrustProperties {

    /**
     * When true, {@code X-Internal-Gateway-Token} must match {@link #token}.
     */
    private boolean enabled = false;

    /**
     * Shared secret configured identically on the gateway ({@code gateway.security.internal-token}).
     */
    private String token = "";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
