package com.petrolpump.gateway.filter;

import com.petrolpump.gateway.config.GatewayRateLimitProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Per-IP sliding window using Redis INCR with a minute bucket key. Returns 429 when the budget is exceeded.
 * Disabled automatically when {@code gateway.rate-limit.enabled=false} or Redis template is unavailable.
 */
@Component
public class RedisIpRateLimitGatewayFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(RedisIpRateLimitGatewayFilter.class);
    private static final DateTimeFormatter MINUTE_BUCKET =
            DateTimeFormatter.ofPattern("yyyyMMddHHmm").withZone(ZoneOffset.UTC);

    private final GatewayRateLimitProperties rateLimitProperties;
    private final Optional<ReactiveStringRedisTemplate> redisTemplate;

    public RedisIpRateLimitGatewayFilter(
            GatewayRateLimitProperties rateLimitProperties,
            ObjectProvider<ReactiveStringRedisTemplate> redisStringTemplateProvider) {
        this.rateLimitProperties = rateLimitProperties;
        this.redisTemplate = Optional.ofNullable(redisStringTemplateProvider.getIfAvailable());
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (HttpMethod.OPTIONS.equals(exchange.getRequest().getMethod())) {
            return chain.filter(exchange);
        }
        if (!rateLimitProperties.isEnabled()) {
            return chain.filter(exchange);
        }
        if (redisTemplate.isEmpty()) {
            log.warn("Rate limiting enabled but ReactiveStringRedisTemplate is missing; skipping rate limit");
            return chain.filter(exchange);
        }
        String ip = resolveClientIp(exchange);
        String minute = MINUTE_BUCKET.format(Instant.now());
        String key = "gw:rl:" + minute + ":" + ip;
        ReactiveStringRedisTemplate redis = redisTemplate.get();
        return redis.opsForValue().increment(key)
                .defaultIfEmpty(0L)
                .flatMap(count -> {
                    if (count == 1L) {
                        return redis.expire(key, Duration.ofMinutes(2)).thenReturn(count);
                    }
                    return Mono.just(count);
                })
                .flatMap(count -> {
                    if (count > rateLimitProperties.getRequestsPerMinute()) {
                        return Mono.error(new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Rate limit exceeded"));
                    }
                    return chain.filter(exchange);
                });
    }

    private static String resolveClientIp(ServerWebExchange exchange) {
        String forwarded = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        if (exchange.getRequest().getRemoteAddress() != null
                && exchange.getRequest().getRemoteAddress().getAddress() != null) {
            return exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        }
        return "unknown";
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
