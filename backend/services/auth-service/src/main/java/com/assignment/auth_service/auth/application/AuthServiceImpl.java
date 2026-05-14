package com.assignment.auth_service.auth.application;

import com.assignment.auth_service.auth.application.dto.LoginRequestDto;
import com.assignment.auth_service.auth.application.dto.LogoutRequestDto;
import com.assignment.auth_service.auth.application.dto.RefreshTokenRequestDto;
import com.assignment.auth_service.auth.application.dto.RegisterRequestDto;
import com.assignment.auth_service.auth.application.dto.TokenResponseDto;
import com.assignment.auth_service.common.exception.ConflictException;
import com.assignment.auth_service.common.exception.UnauthorizedException;
import com.assignment.auth_service.config.JwtProperties;
import com.assignment.auth_service.jwt.JwtService;
import com.assignment.auth_service.refresh.infrastructure.RefreshTokenJpaRepository;
import com.assignment.auth_service.refresh.infrastructure.entity.RefreshTokenEntity;
import com.assignment.auth_service.user.domain.Role;
import com.assignment.auth_service.user.infrastructure.UserJpaRepository;
import com.assignment.auth_service.user.infrastructure.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

/**
 * Orchestrates credential checks, JWT issuance, and refresh-token lifecycle (persist, refresh, revoke).
 */
@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private static final String BEARER = "Bearer";

    private final UserJpaRepository userJpaRepository;
    private final RefreshTokenJpaRepository refreshTokenJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final SecureRandom secureRandom = new SecureRandom();

    public AuthServiceImpl(
            UserJpaRepository userJpaRepository,
            RefreshTokenJpaRepository refreshTokenJpaRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            JwtProperties jwtProperties) {
        this.userJpaRepository = userJpaRepository;
        this.refreshTokenJpaRepository = refreshTokenJpaRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public void register(RegisterRequestDto request) {
        String email = normalizeEmail(request.getEmail());
        if (userJpaRepository.existsByEmailIgnoreCase(email)) {
            throw new ConflictException("Email already registered");
        }
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.EMPLOYEE);
        user.setActive(true);
        userJpaRepository.save(user);
        log.info("Registered new user email={}", email);
    }

    @Override
    public TokenResponseDto login(LoginRequestDto request) {
        String email = normalizeEmail(request.getEmail());
        UserEntity user = userJpaRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
        if (!user.isActive()) {
            throw new UnauthorizedException("Account disabled");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }
        String access = jwtService.createAccessToken(user.getId(), user.getEmail(), user.getRole());
        String refresh = createAndPersistRefreshToken(user);
        log.info("User logged in id={}", user.getId());
        return new TokenResponseDto(access, refresh, BEARER, jwtService.accessTokenExpiresInSeconds());
    }

    @Override
    public TokenResponseDto refresh(RefreshTokenRequestDto request) {
        RefreshTokenEntity entity = refreshTokenJpaRepository.findByTokenFetchUser(request.getRefreshToken())
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));
        if (entity.isRevoked()) {
            throw new UnauthorizedException("Refresh token revoked");
        }
        if (entity.getExpiryDate().isBefore(Instant.now())) {
            throw new UnauthorizedException("Refresh token expired");
        }
        UserEntity user = entity.getUser();
        if (!user.isActive()) {
            throw new UnauthorizedException("Account disabled");
        }
        String access = jwtService.createAccessToken(user.getId(), user.getEmail(), user.getRole());
        log.debug("Issued new access token for userId={}", user.getId());
        return new TokenResponseDto(access, entity.getToken(), BEARER, jwtService.accessTokenExpiresInSeconds());
    }

    @Override
    public void logout(LogoutRequestDto request) {
        refreshTokenJpaRepository.findByTokenFetchUser(request.getRefreshToken())
                .ifPresentOrElse(
                        entity -> {
                            entity.setRevoked(true);
                            refreshTokenJpaRepository.save(entity);
                            log.info("Refresh token revoked id={}", entity.getId());
                        },
                        () -> log.debug("Logout called with unknown refresh token"));
    }

    private String createAndPersistRefreshToken(UserEntity user) {
        String raw = generateSecureToken();
        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setToken(raw);
        entity.setUser(user);
        entity.setRevoked(false);
        entity.setExpiryDate(Instant.now().plus(jwtProperties.getRefreshTokenDays(), ChronoUnit.DAYS));
        refreshTokenJpaRepository.save(entity);
        return raw;
    }

    private String generateSecureToken() {
        byte[] bytes = new byte[48];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }
}
