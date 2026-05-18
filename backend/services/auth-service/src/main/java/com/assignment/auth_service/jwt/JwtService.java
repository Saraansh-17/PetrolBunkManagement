package com.assignment.auth_service.jwt;

import com.assignment.auth_service.common.exception.ExpiredTokenException;
import com.assignment.auth_service.common.exception.InvalidTokenException;
import com.assignment.auth_service.config.JwtProperties;
import com.assignment.auth_service.user.domain.Role;
import com.assignment.auth_service.user.infrastructure.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Issues and validates access JWTs. Subject ({@code sub}) is the business employeeId; role and email are claims.
 */
@Service
public class JwtService {

    public static final String CLAIM_ROLE = "role";
    public static final String CLAIM_EMAIL = "email";
    public static final String CLAIM_USER_ID = "userId";

    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String createAccessToken(UserEntity user) {
        Instant now = Instant.now();
        Instant exp = now.plus(jwtProperties.getAccessTokenMinutes(), ChronoUnit.MINUTES);
        return Jwts.builder()
                .issuer(jwtProperties.getIssuer())
                .subject(user.getEmployeeId())
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .claim(CLAIM_ROLE, user.getRole().name())
                .claim(CLAIM_EMAIL, user.getEmail())
                .claim(CLAIM_USER_ID, user.getId())
                .signWith(signingKey())
                .compact();
    }

    public Claims parseAndValidate(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(signingKey())
                    .requireIssuer(jwtProperties.getIssuer())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException ex) {
            throw new ExpiredTokenException("Access token expired", ex);
        } catch (SignatureException | IllegalArgumentException ex) {
            throw new InvalidTokenException("Invalid access token", ex);
        } catch (Exception ex) {
            throw new InvalidTokenException("Unable to parse access token", ex);
        }
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
