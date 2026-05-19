package com.assignment.hr_service.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GatewayTrustProperties.class)
public class GatewayTrustConfiguration {
}
