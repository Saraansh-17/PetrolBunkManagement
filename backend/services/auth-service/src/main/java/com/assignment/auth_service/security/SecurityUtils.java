package com.assignment.auth_service.security;

import com.assignment.auth_service.common.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static EmployeeUserDetails requireAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof EmployeeUserDetails details)) {
            throw new UnauthorizedException("Not authenticated");
        }
        return details;
    }
}
