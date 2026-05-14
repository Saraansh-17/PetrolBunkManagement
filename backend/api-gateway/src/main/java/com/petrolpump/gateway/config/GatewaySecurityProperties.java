package com.petrolpump.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Gateway security: JWT validation, internal service token, and public route patterns.
 */
@ConfigurationProperties(prefix = "gateway.security")
public class GatewaySecurityProperties {

    /**
     * Shared secret the gateway adds so downstream services can reject direct calls without the secret.
     */
    private String internalToken = "";

    /**
     * Ant-style patterns that skip JWT validation (e.g. /auth/**, /gateway/health).
     */
    private List<String> publicPatterns = new ArrayList<>(List.of(
            "/auth/**",
            "/gateway/health",
            "/actuator/**"
    ));

    public String getInternalToken() {
        return internalToken;
    }

    public void setInternalToken(String internalToken) {
        this.internalToken = internalToken;
    }

    public List<String> getPublicPatterns() {
        return publicPatterns;
    }

    public void setPublicPatterns(List<String> publicPatterns) {
        this.publicPatterns = publicPatterns;
    }
}
