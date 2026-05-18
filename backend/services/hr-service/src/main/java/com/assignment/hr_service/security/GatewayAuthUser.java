package com.assignment.hr_service.security;

public class GatewayAuthUser {

    private final Long userId;
    private final String employeeId;
    private final String email;
    private final String role;

    public GatewayAuthUser(Long userId, String employeeId, String email, String role) {
        this.userId = userId;
        this.employeeId = employeeId;
        this.email = email;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmployeeId() {
        return employeeId;
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
