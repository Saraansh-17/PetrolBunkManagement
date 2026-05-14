package com.petrolpump.gateway.security;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

/**
 * Coarse-grained role checks at the edge. Fine-grained rules remain in domain services (HR).
 */
@Component
public class GatewayRouteAuthorizationEvaluator {

    public void assertEmployeeMutationsAllowed(HttpMethod method, String path, String role) {
        if (!path.startsWith("/employees")) {
            return;
        }
        if (method == HttpMethod.POST || method == HttpMethod.PUT
                || method == HttpMethod.PATCH || method == HttpMethod.DELETE) {
            requireAdmin(role, "Employee mutations require ADMIN");
        }
        if (method == HttpMethod.GET && "/employees".equals(path)) {
            requireAdmin(role, "Listing employees requires ADMIN");
        }
    }

    public void assertAttendanceGatewayRules(HttpMethod method, String path, String role) {
        if (!path.startsWith("/attendance")) {
            return;
        }
        if (method == HttpMethod.GET && "/attendance".equals(path)) {
            requireAdmin(role, "Listing all attendance requires ADMIN");
        }
        if (method == HttpMethod.GET && path.startsWith("/attendance/date")) {
            requireAdmin(role, "Attendance by date requires ADMIN");
        }
    }

    private static void requireAdmin(String role, String message) {
        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN,
                    message
            );
        }
    }
}
