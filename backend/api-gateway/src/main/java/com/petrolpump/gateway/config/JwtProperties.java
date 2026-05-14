package com.petrolpump.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JWT verification settings — must align with auth-service signing configuration.
 */
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret;

    private String issuer = "petrol-pump-auth";

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
}
