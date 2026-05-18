package com.assignment.hr_service.security;

import com.assignment.hr_service.config.GatewayTrustProperties;
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

/**
 * Validates service-to-service calls on /hr/internal/** using the same gateway internal token.
 */
@Component
@Order(0)
public class InternalServiceAuthFilter extends OncePerRequestFilter {

    private final GatewayTrustProperties gatewayTrustProperties;

    public InternalServiceAuthFilter(GatewayTrustProperties gatewayTrustProperties) {
        this.gatewayTrustProperties = gatewayTrustProperties;
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/hr/internal");
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String expected = gatewayTrustProperties.getToken();
        if (expected == null || expected.isBlank()) {
            response.sendError(HttpStatus.SERVICE_UNAVAILABLE.value(), "Internal trust misconfigured");
            return;
        }
        String provided = request.getHeader(GatewayAuthConstants.HEADER_INTERNAL_GATEWAY_TOKEN);
        if (provided == null || !expected.equals(provided)) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "Invalid internal service token");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
