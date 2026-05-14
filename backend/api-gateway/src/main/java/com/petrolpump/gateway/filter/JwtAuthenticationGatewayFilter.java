package com.petrolpump.gateway.filter;

import com.petrolpump.gateway.config.GatewaySecurityProperties;
import com.petrolpump.gateway.security.GatewayJwtValidator;
import com.petrolpump.gateway.security.GatewayRouteAuthorizationEvaluator;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Validates JWTs for secured routes, enforces coarse role rules, forwards identity headers to downstream services,
 * and strips the Authorization header to avoid leaking bearer tokens beyond the gateway.
 */
@Component
public class JwtAuthenticationGatewayFilter implements GlobalFilter, Ordered {

    public static final String HEADER_USER_ID = "X-User-Id";
    public static final String HEADER_USER_EMAIL = "X-User-Email";
    public static final String HEADER_USER_ROLE = "X-User-Role";
    public static final String HEADER_INTERNAL_GATEWAY_TOKEN = "X-Internal-Gateway-Token";

    private final GatewaySecurityProperties securityProperties;
    private final AntPathMatcher antPathMatcher;
    private final GatewayJwtValidator jwtValidator;
    private final GatewayRouteAuthorizationEvaluator routeAuthorizationEvaluator;

    public JwtAuthenticationGatewayFilter(
            GatewaySecurityProperties securityProperties,
            AntPathMatcher antPathMatcher,
            GatewayJwtValidator jwtValidator,
            GatewayRouteAuthorizationEvaluator routeAuthorizationEvaluator) {
        this.securityProperties = securityProperties;
        this.antPathMatcher = antPathMatcher;
        this.jwtValidator = jwtValidator;
        this.routeAuthorizationEvaluator = routeAuthorizationEvaluator;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (HttpMethod.OPTIONS.equals(request.getMethod())) {
            return chain.filter(exchange);
        }
        String path = request.getURI().getPath();
        if (path == null) {
            path = "";
        }
        HttpMethod method = request.getMethod() != null ? request.getMethod() : HttpMethod.GET;

        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }
        if (!requiresJwt(path)) {
            return chain.filter(exchange);
        }

        String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        Claims claims = jwtValidator.validateAndParse(authorization);
        String userId = String.valueOf(claims.get(GatewayJwtValidator.CLAIM_USER_ID));
        String email = claims.get(GatewayJwtValidator.CLAIM_EMAIL, String.class);
        String role = claims.get(GatewayJwtValidator.CLAIM_ROLE, String.class);

        routeAuthorizationEvaluator.assertEmployeeMutationsAllowed(method, path, role);
        routeAuthorizationEvaluator.assertAttendanceGatewayRules(method, path, role);

        ServerHttpRequest mutated = request.mutate()
                .headers(headers -> {
                    headers.remove(HttpHeaders.AUTHORIZATION);
                    headers.set(HEADER_USER_ID, userId);
                    headers.set(HEADER_USER_EMAIL, email != null ? email : "");
                    headers.set(HEADER_USER_ROLE, role != null ? role : "");
                    String internal = securityProperties.getInternalToken();
                    if (internal != null && !internal.isBlank()) {
                        headers.set(HEADER_INTERNAL_GATEWAY_TOKEN, internal);
                    }
                })
                .build();

        return chain.filter(exchange.mutate().request(mutated).build());
    }

    private boolean isPublicPath(String path) {
        for (String pattern : securityProperties.getPublicPatterns()) {
            if (antPathMatcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

    private static boolean requiresJwt(String path) {
        return path.startsWith("/employees")
                || path.startsWith("/attendance")
                || path.startsWith("/fuel")
                || path.startsWith("/inventory");
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 10;
    }
}
