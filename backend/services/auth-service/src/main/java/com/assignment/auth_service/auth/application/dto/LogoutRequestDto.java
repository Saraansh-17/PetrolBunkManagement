package com.assignment.auth_service.auth.application.dto;

import jakarta.validation.constraints.NotBlank;

public class LogoutRequestDto {

    @NotBlank
    private String refreshToken;

    public LogoutRequestDto() {
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
