package com.assignment.auth_service.auth.application;

import com.assignment.auth_service.auth.application.dto.AuthResponse;
import com.assignment.auth_service.auth.application.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    AuthResponse login(LoginRequest request, HttpServletResponse response);

    AuthResponse getCurrentUser();

    void logout(HttpServletRequest request, HttpServletResponse response);

    AuthResponse refresh(HttpServletRequest request, HttpServletResponse response);
}
