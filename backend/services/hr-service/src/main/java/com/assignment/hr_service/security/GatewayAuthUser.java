package com.assignment.hr_service.security;

/**
 * Identity forwarded by the gateway (never trust client-supplied user id without gateway validation upstream).
 */
public class GatewayAuthUser {

    private final Long userId;
    private final String email;
    private final String role;

    public GatewayAuthUser(Long userId, String email, String role) {
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    public boolean isEmployee() {
        return "EMPLOYEE".equalsIgnoreCase(role);
    }
}
