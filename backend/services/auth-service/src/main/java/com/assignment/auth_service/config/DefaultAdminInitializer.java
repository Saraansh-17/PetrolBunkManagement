package com.assignment.auth_service.config;

import com.assignment.auth_service.user.domain.Role;
import com.assignment.auth_service.user.infrastructure.UserJpaRepository;
import com.assignment.auth_service.user.infrastructure.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Seeds a default ADMIN account when none exists (development / first boot).
 */
@Component
public class DefaultAdminInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DefaultAdminInitializer.class);

    private final AdminBootstrapProperties adminBootstrapProperties;
    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;

    public DefaultAdminInitializer(
            AdminBootstrapProperties adminBootstrapProperties,
            UserJpaRepository userJpaRepository,
            PasswordEncoder passwordEncoder) {
        this.adminBootstrapProperties = adminBootstrapProperties;
        this.userJpaRepository = userJpaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!adminBootstrapProperties.isEnabled()) {
            return;
        }
        if (userJpaRepository.findByEmailIgnoreCase(adminBootstrapProperties.getEmail().trim().toLowerCase()).isPresent()) {
            return;
        }
        UserEntity admin = new UserEntity();
        admin.setEmployeeId(adminBootstrapProperties.getEmployeeId().trim());
        admin.setName(adminBootstrapProperties.getName());
        admin.setEmail(adminBootstrapProperties.getEmail().trim().toLowerCase());
        admin.setPhoneNumber(adminBootstrapProperties.getPhoneNumber());
        String rawPassword = adminBootstrapProperties.getPassword();
        if (rawPassword == null || rawPassword.isBlank()) {
            log.warn("Admin bootstrap skipped: password not configured");
            return;
        }
        admin.setPassword(passwordEncoder.encode(rawPassword));
        admin.setRole(Role.ADMIN);
        admin.setActive(true);
        userJpaRepository.save(admin);
        log.info("Default ADMIN account created for email={}", admin.getEmail());
    }
}
