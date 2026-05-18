package com.petrolpump.gateway.filter;

import com.petrolpump.gateway.config.GatewayCookieProperties;
import com.petrolpump.gateway.config.GatewaySecurityProperties;
import com.petrolpump.gateway.security.GatewayJwtValidator;
import com.petrolpump.gateway.security.GatewayRouteAuthorizationEvaluator;
import com.petrolpump.gateway.util.GatewayPathUtils;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Validates JWT from Authorization header or access_token cookie, enforces edge role rules,
 * forwards trusted identity headers downstream.
 */
@Component
public class JwtAuthenticationGatewayFilter implements GlobalFilter, Ordered {

    public static final String HEADER_USER_ID = "X-User-Id";
    public static final String HEADER_USER_EMAIL = "X-User-Email";
    public static final String HEADER_USER_ROLE = "X-User-Role";
    public static final String HEADER_EMPLOYEE_ID = "X-Employee-Id";
    public static final String HEADER_INTERNAL_GATEWAY_TOKEN = "X-Internal-Gateway-Token";

    private final GatewaySecurityProperties securityProperties;
    private final GatewayCookieProperties cookieProperties;
    private final AntPathMatcher antPathMatcher;
    private final GatewayJwtValidator jwtValidator;
    private final GatewayRouteAuthorizationEvaluator routeAuthorizationEvaluator;

    public JwtAuthenticationGatewayFilter(
            GatewaySecurityProperties securityProperties,
            GatewayCookieProperties cookieProperties,
            AntPathMatcher antPathMatcher,
            GatewayJwtValidator jwtValidator,
            GatewayRouteAuthorizationEvaluator routeAuthorizationEvaluator) {
        this.securityProperties = securityProperties;
        this.cookieProperties = cookieProperties;
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

        String path = GatewayPathUtils.normalize(request.getURI().getPath());
        HttpMethod method = request.getMethod() != null ? request.getMethod() : HttpMethod.GET;

        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }
        if (!requiresJwt(path)) {
            return chain.filter(exchange);
        }

        String token = resolveToken(request);
        Claims claims = jwtValidator.validateAndParse(token);

        String employeeId = claims.getSubject();
        String userId = String.valueOf(claims.get(GatewayJwtValidator.CLAIM_USER_ID));
        String email = claims.get(GatewayJwtValidator.CLAIM_EMAIL, String.class);
        String role = claims.get(GatewayJwtValidator.CLAIM_ROLE, String.class);

        routeAuthorizationEvaluator.authorize(method, path, role);

        ServerHttpRequest mutated = request.mutate()
                .headers(headers -> {
                    headers.remove(HttpHeaders.AUTHORIZATION);
                    headers.set(HEADER_USER_ID, userId);
                    headers.set(HEADER_EMPLOYEE_ID, employeeId != null ? employeeId : "");
                    headers.set(HEADER_USER_EMAIL, email != null ? email : "");
                    headers.set(HEADER_USER_ROLE, role != null ? role : "");
                    String internal = securityProperties.getInternalToken();
                    if (StringUtils.hasText(internal)) {
                        headers.set(HEADER_INTERNAL_GATEWAY_TOKEN, internal);
                    }
                })
                .build();

        return chain.filter(exchange.mutate().request(mutated).build());
    }

    private String resolveToken(ServerHttpRequest request) {
        String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authorization)) {
            return authorization;
        }
        if (request.getCookies().containsKey(cookieProperties.getAccessTokenName())) {
            var cookie = request.getCookies().getFirst(cookieProperties.getAccessTokenName());
            if (cookie != null && StringUtils.hasText(cookie.getValue())) {
                return cookie.getValue();
            }
        }
        return null;
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
        return path.startsWith("/auth/employees")
                || path.startsWith("/hr/employees")
                || path.startsWith("/attendance")
                || path.startsWith("/fuel")
                || path.startsWith("/inventory");
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 10;
    }
}
