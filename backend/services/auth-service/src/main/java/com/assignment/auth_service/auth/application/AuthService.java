package com.assignment.auth_service.auth.application;

import com.assignment.auth_service.auth.application.dto.LoginRequestDto;
import com.assignment.auth_service.auth.application.dto.LogoutRequestDto;
import com.assignment.auth_service.auth.application.dto.RefreshTokenRequestDto;
import com.assignment.auth_service.auth.application.dto.RegisterRequestDto;
import com.assignment.auth_service.auth.application.dto.TokenResponseDto;

/**
 * Authentication use cases: register, login, refresh, logout.
 */
public interface AuthService {

    void register(RegisterRequestDto request);

    TokenResponseDto login(LoginRequestDto request);

    TokenResponseDto refresh(RefreshTokenRequestDto request);

    void logout(LogoutRequestDto request);
}
