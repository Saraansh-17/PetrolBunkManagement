package com.petrolpump.gateway.security;

import com.petrolpump.gateway.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * Validates access tokens issued by auth-service (signature, issuer, expiry).
 */
@Component
public class GatewayJwtValidator {

    public static final String CLAIM_USER_ID = "userId";
    public static final String CLAIM_EMAIL = "email";
    public static final String CLAIM_ROLE = "role";

    private final JwtProperties jwtProperties;

    public GatewayJwtValidator(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public Claims validateAndParse(String bearerToken) {
        if (bearerToken == null || bearerToken.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing bearer token");
        }
        String token = bearerToken.startsWith("Bearer ") ? bearerToken.substring(7).trim() : bearerToken.trim();
        try {
            return Jwts.parser()
                    .verifyWith(signingKey())
                    .requireIssuer(jwtProperties.getIssuer())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token", ex);
        }
    }

    private SecretKey signingKey() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalStateException("JWT secret must be at least 256 bits (32 bytes) for HS256");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
