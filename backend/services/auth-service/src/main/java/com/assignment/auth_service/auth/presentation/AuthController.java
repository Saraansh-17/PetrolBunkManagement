package com.assignment.auth_service.auth.presentation;

import com.assignment.auth_service.auth.application.AuthService;
import com.assignment.auth_service.auth.application.dto.LoginRequestDto;
import com.assignment.auth_service.auth.application.dto.LogoutRequestDto;
import com.assignment.auth_service.auth.application.dto.RefreshTokenRequestDto;
import com.assignment.auth_service.auth.application.dto.RegisterRequestDto;
import com.assignment.auth_service.auth.application.dto.TokenResponseDto;
import com.assignment.auth_service.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST entrypoints for authentication. Login/refresh return the compact OAuth2-style body required by clients.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequestDto request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", null));
    }

    /**
     * Returns access + refresh tokens (contract used by SPA/mobile clients).
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        TokenResponseDto body = authService.login(request);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refresh(@Valid @RequestBody RefreshTokenRequestDto request) {
        TokenResponseDto body = authService.refresh(request);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody LogoutRequestDto request) {
        authService.logout(request);
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully", null));
    }
}
