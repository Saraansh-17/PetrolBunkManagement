package com.assignment.auth_service.security;

import com.assignment.auth_service.config.CookieProperties;
import com.assignment.auth_service.config.JwtProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

/**
 * Sets and clears HttpOnly JWT cookies (access + refresh).
 */
@Service
public class CookieTokenService {

    private final CookieProperties cookieProperties;
    private final JwtProperties jwtProperties;

    public CookieTokenService(CookieProperties cookieProperties, JwtProperties jwtProperties) {
        this.cookieProperties = cookieProperties;
        this.jwtProperties = jwtProperties;
    }

    public void writeAccessTokenCookie(HttpServletResponse response, String accessToken) {
        response.addHeader(HttpHeaders.SET_COOKIE, buildCookie(
                cookieProperties.getAccessTokenName(),
                accessToken,
                Duration.ofMinutes(jwtProperties.getAccessTokenMinutes())).toString());
    }

    public void writeRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        response.addHeader(HttpHeaders.SET_COOKIE, buildCookie(
                cookieProperties.getRefreshTokenName(),
                refreshToken,
                Duration.ofDays(jwtProperties.getRefreshTokenDays())).toString());
    }

    public void clearAuthCookies(HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE, buildCookie(
                cookieProperties.getAccessTokenName(), "", Duration.ZERO).toString());
        response.addHeader(HttpHeaders.SET_COOKIE, buildCookie(
                cookieProperties.getRefreshTokenName(), "", Duration.ZERO).toString());
    }

    public Optional<String> readAccessToken(HttpServletRequest request) {
        return readCookie(request, cookieProperties.getAccessTokenName());
    }

    public Optional<String> readRefreshToken(HttpServletRequest request) {
        return readCookie(request, cookieProperties.getRefreshTokenName());
    }

    private ResponseCookie buildCookie(String name, String value, Duration maxAge) {
        return ResponseCookie.from(name, value)
                .path(cookieProperties.getPath())
                .httpOnly(cookieProperties.isHttpOnly())
                .secure(cookieProperties.isSecure())
                .sameSite(cookieProperties.getSameSite())
                .maxAge(maxAge)
                .build();
    }

    private static Optional<String> readCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(c -> name.equals(c.getName()))
                .map(Cookie::getValue)
                .filter(v -> v != null && !v.isBlank())
                .findFirst();
    }
}
