package com.assignment.auth_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Default ADMIN account created on startup when no admin exists.
 */
@ConfigurationProperties(prefix = "auth.admin.bootstrap")
public class AdminBootstrapProperties {

    private boolean enabled = true;

    private String employeeId = "ADMIN001";

    private String name = "System Administrator";

    private String email = "admin@gmail.com";

    private String password = "";

    private String phoneNumber = "0000000000";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
