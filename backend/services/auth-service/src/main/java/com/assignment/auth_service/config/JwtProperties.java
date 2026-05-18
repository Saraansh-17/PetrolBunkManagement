package com.assignment.auth_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Externalized JWT settings; secrets must come from environment in production.
 */
@ConfigurationProperties(prefix = "jwt")
public class  JwtProperties {

    /**
     * HMAC key for signing access tokens (HS256). Use a long random value via {@code JWT_SECRET}.
     */
    private String secret;

    private String issuer = "petrol-pump-auth";

    private int accessTokenMinutes = 15;

    private int refreshTokenDays = 7;

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

    public int getAccessTokenMinutes() {
        return accessTokenMinutes;
    }

    public void setAccessTokenMinutes(int accessTokenMinutes) {
        this.accessTokenMinutes = accessTokenMinutes;
    }

    public int getRefreshTokenDays() {
        return refreshTokenDays;
    }

    public void setRefreshTokenDays(int refreshTokenDays) {
        this.refreshTokenDays = refreshTokenDays;
    }
}
