package com.assignment.hr_service.security;

/**
 * Header names populated by the API Gateway after JWT validation.
 */
public final class GatewayAuthConstants {

    public static final String HEADER_USER_ID = "X-User-Id";
    public static final String HEADER_USER_EMAIL = "X-User-Email";
    public static final String HEADER_USER_ROLE = "X-User-Role";
    public static final String HEADER_INTERNAL_GATEWAY_TOKEN = "X-Internal-Gateway-Token";

    public static final String REQUEST_ATTR_GATEWAY_USER = "com.assignment.hr_service.GATEWAY_USER";

    private GatewayAuthConstants() {
    }
}
