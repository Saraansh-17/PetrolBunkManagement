package com.assignment.auth_service.jwt;

import com.assignment.auth_service.config.JwtProperties;
import com.assignment.auth_service.user.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Issues and parses access JWTs. Claims: userId, email, role (plus subject = userId).
 * Refresh tokens are opaque DB-backed values managed elsewhere.
 */
@Service
public class JwtService {

    public static final String CLAIM_USER_ID = "userId";
    public static final String CLAIM_EMAIL = "email";
    public static final String CLAIM_ROLE = "role";

    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String createAccessToken(Long userId, String email, Role role) {
        Instant now = Instant.now();
        Instant exp = now.plus(jwtProperties.getAccessTokenMinutes(), ChronoUnit.MINUTES);
        return Jwts.builder()
                .issuer(jwtProperties.getIssuer())
                .subject(String.valueOf(userId))
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .claim(CLAIM_USER_ID, userId)
                .claim(CLAIM_EMAIL, email)
                .claim(CLAIM_ROLE, role.name())
                .signWith(signingKey())
                .compact();
    }

    /**
     * Validates signature, issuer, and expiration — used by auth-service for token inspection flows.
     */
    public Claims parseAndValidate(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .requireIssuer(jwtProperties.getIssuer())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public int accessTokenExpiresInSeconds() {
        return jwtProperties.getAccessTokenMinutes() * 60;
    }

    private SecretKey signingKey() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalStateException("JWT secret must be at least 256 bits (32 bytes) for HS256");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
