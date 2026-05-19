package com.assignment.hr_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Enables Spring Data JPA auditing so infrastructure entities can populate created/updated timestamps
 * without leaking that concern into the domain layer.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
