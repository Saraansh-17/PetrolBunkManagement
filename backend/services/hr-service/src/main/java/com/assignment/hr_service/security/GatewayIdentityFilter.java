package com.assignment.hr_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(2)
public class GatewayIdentityFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri.startsWith("/hr/internal")) {
            return true;
        }
        return !uri.startsWith("/hr/employees") && !uri.startsWith("/attendance");
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String userIdHeader = request.getHeader(GatewayAuthConstants.HEADER_USER_ID);
        String roleHeader = request.getHeader(GatewayAuthConstants.HEADER_USER_ROLE);
        String employeeIdHeader = request.getHeader(GatewayAuthConstants.HEADER_EMPLOYEE_ID);
        if (userIdHeader == null || userIdHeader.isBlank()
                || roleHeader == null || roleHeader.isBlank()
                || employeeIdHeader == null || employeeIdHeader.isBlank()) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Missing gateway identity headers");
            return;
        }
        try {
            Long userId = Long.parseLong(userIdHeader.trim());
            String email = request.getHeader(GatewayAuthConstants.HEADER_USER_EMAIL);
            GatewayAuthUser user = new GatewayAuthUser(
                    userId,
                    employeeIdHeader.trim(),
                    email != null ? email : "",
                    roleHeader.trim());
            request.setAttribute(GatewayAuthConstants.REQUEST_ATTR_GATEWAY_USER, user);
            filterChain.doFilter(request, response);
        } catch (NumberFormatException ex) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid user id header");
        }
    }
}
