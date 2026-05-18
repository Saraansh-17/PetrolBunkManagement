package com.assignment.auth_service.auth.application.dto;

public class AuthUserResponse {

    private String employeeId;
    private String name;
    private String email;
    private String role;

    public AuthUserResponse() {
    }

    public AuthUserResponse(String employeeId, String name, String email, String role) {
        this.employeeId = employeeId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
