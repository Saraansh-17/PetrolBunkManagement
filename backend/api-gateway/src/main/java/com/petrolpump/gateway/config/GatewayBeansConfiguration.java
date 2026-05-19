package com.petrolpump.gateway.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

@Configuration
@EnableConfigurationProperties({
        JwtProperties.class,
        GatewaySecurityProperties.class,
        GatewayRateLimitProperties.class,
        GatewayCookieProperties.class
})
public class GatewayBeansConfiguration {

    @Bean
    public AntPathMatcher antPathMatcher() {
        return new AntPathMatcher();
    }
}
