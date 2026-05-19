package com.petrolpump.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "gateway.security")
public class GatewaySecurityProperties {

    private String internalToken = "";

    private List<String> publicPatterns = new ArrayList<>(List.of(
            "/auth/login",
            "/auth/refresh",
            "/gateway/health"
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
