package com.assignment.auth_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * HttpOnly cookie settings for JWT access and refresh tokens.
 */
@ConfigurationProperties(prefix = "auth.cookie")
public class CookieProperties {

    private String accessTokenName = "access_token";

    private String refreshTokenName = "refresh_token";

    private String path = "/";

    private boolean httpOnly = true;

    private boolean secure = false;

    private String sameSite = "Strict";

    public String getAccessTokenName() {
        return accessTokenName;
    }

    public void setAccessTokenName(String accessTokenName) {
        this.accessTokenName = accessTokenName;
    }

    public String getRefreshTokenName() {
        return refreshTokenName;
    }

    public void setRefreshTokenName(String refreshTokenName) {
        this.refreshTokenName = refreshTokenName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public String getSameSite() {
        return sameSite;
    }

    public void setSameSite(String sameSite) {
        this.sameSite = sameSite;
    }
}
