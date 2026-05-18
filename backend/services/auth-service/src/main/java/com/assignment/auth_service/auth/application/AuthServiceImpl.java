package com.assignment.auth_service.auth.application;

import com.assignment.auth_service.auth.application.dto.AuthResponse;
import com.assignment.auth_service.auth.application.dto.LoginRequest;
import com.assignment.auth_service.common.exception.UnauthorizedException;
import com.assignment.auth_service.config.JwtProperties;
import com.assignment.auth_service.jwt.JwtService;
import com.assignment.auth_service.refresh.infrastructure.RefreshTokenJpaRepository;
import com.assignment.auth_service.refresh.infrastructure.entity.RefreshTokenEntity;
import com.assignment.auth_service.security.CookieTokenService;
import com.assignment.auth_service.security.EmployeeUserDetails;
import com.assignment.auth_service.security.SecurityUtils;
import com.assignment.auth_service.user.application.mapper.UserDtoMapper;
import com.assignment.auth_service.user.infrastructure.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CookieTokenService cookieTokenService;
    private final RefreshTokenJpaRepository refreshTokenJpaRepository;
    private final JwtProperties jwtProperties;
    private final UserDtoMapper userDtoMapper;
    private final SecureRandom secureRandom = new SecureRandom();

    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            CookieTokenService cookieTokenService,
            RefreshTokenJpaRepository refreshTokenJpaRepository,
            JwtProperties jwtProperties,
            UserDtoMapper userDtoMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.cookieTokenService = cookieTokenService;
        this.refreshTokenJpaRepository = refreshTokenJpaRepository;
        this.jwtProperties = jwtProperties;
        this.userDtoMapper = userDtoMapper;
    }

    @Override
    public AuthResponse login(LoginRequest request, HttpServletResponse response) {
        String email = normalizeEmail(request.getEmail());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword()));
        EmployeeUserDetails principal = (EmployeeUserDetails) authentication.getPrincipal();
        UserEntity user = principal.getUser();

        refreshTokenJpaRepository.revokeAllActiveForUser(user.getId());
        issueTokenCookies(user, response);
        log.info("User logged in employeeId={}", user.getEmployeeId());
        return new AuthResponse(true, userDtoMapper.toAuthUserResponse(user));
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse getCurrentUser() {
        EmployeeUserDetails principal = SecurityUtils.requireAuthenticatedUser();
        return new AuthResponse(true, userDtoMapper.toAuthUserResponse(principal.getUser()));
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        cookieTokenService.readRefreshToken(request).ifPresent(refreshToken ->
                refreshTokenJpaRepository.findByTokenFetchUser(refreshToken).ifPresent(entity -> {
                    entity.setRevoked(true);
                    refreshTokenJpaRepository.save(entity);
                }));
        cookieTokenService.clearAuthCookies(response);
        log.info("User logged out");
    }

    @Override
    public AuthResponse refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookieTokenService.readRefreshToken(request)
                .orElseThrow(() -> new UnauthorizedException("Refresh token cookie missing"));

        RefreshTokenEntity entity = refreshTokenJpaRepository.findByTokenFetchUser(refreshToken)
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

        String access = jwtService.createAccessToken(user);
        cookieTokenService.writeAccessTokenCookie(response, access);
        return new AuthResponse(true, userDtoMapper.toAuthUserResponse(user));
    }

    private void issueTokenCookies(UserEntity user, HttpServletResponse response) {
        String access = jwtService.createAccessToken(user);
        String refresh = createAndPersistRefreshToken(user);
        cookieTokenService.writeAccessTokenCookie(response, access);
        cookieTokenService.writeRefreshTokenCookie(response, refresh);
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
