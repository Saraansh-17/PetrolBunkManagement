package com.petrolpump.gateway.security;

import com.petrolpump.gateway.util.GatewayPathUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

/**
 * Coarse-grained role checks at the edge. Paths must be normalized before calling.
 */
@Component
public class GatewayRouteAuthorizationEvaluator {

    public void authorize(HttpMethod method, String rawPath, String role) {
        String path = GatewayPathUtils.normalize(rawPath);
        assertAuthEmployeeRoutes(method, path, role);
        assertHrEmployeeRoutes(method, path, role);
        assertAttendanceRoutes(method, path, role);
    }

    private void assertAuthEmployeeRoutes(HttpMethod method, String path, String role) {
        if (!path.startsWith("/auth/employees")) {
            return;
        }
        requireAdmin(role, "Auth employee management requires ADMIN");
    }

    private void assertHrEmployeeRoutes(HttpMethod method, String path, String role) {
        if (!path.startsWith("/hr/employees")) {
            return;
        }
        if (path.startsWith("/hr/internal")) {
            return;
        }
        if (method == HttpMethod.POST || method == HttpMethod.PUT
                || method == HttpMethod.PATCH || method == HttpMethod.DELETE) {
            requireAdmin(role, "HR employee mutations require ADMIN");
        }
        if (method == HttpMethod.GET && "/hr/employees".equals(path)) {
            requireAdmin(role, "Listing HR employees requires ADMIN");
        }
    }

    private void assertAttendanceRoutes(HttpMethod method, String path, String role) {
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
