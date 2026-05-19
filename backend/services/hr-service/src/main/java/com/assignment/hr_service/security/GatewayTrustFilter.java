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
 * Rejects proxied API calls without the shared gateway internal token.
 */
@Component
@Order(1)
public class GatewayTrustFilter extends OncePerRequestFilter {

    private final GatewayTrustProperties gatewayTrustProperties;

    public GatewayTrustFilter(GatewayTrustProperties gatewayTrustProperties) {
        this.gatewayTrustProperties = gatewayTrustProperties;
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        if (!gatewayTrustProperties.isEnabled()) {
            return true;
        }
        String uri = request.getRequestURI();
        return !uri.startsWith("/hr/employees")
                && !uri.startsWith("/attendance");
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String expected = gatewayTrustProperties.getToken();
        if (expected == null || expected.isBlank()) {
            response.sendError(HttpStatus.SERVICE_UNAVAILABLE.value(), "Gateway trust misconfigured");
            return;
        }
        String provided = request.getHeader(GatewayAuthConstants.HEADER_INTERNAL_GATEWAY_TOKEN);
        if (provided == null || !expected.equals(provided)) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "Invalid gateway token");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
