package com.assignment.auth_service.security;

import com.assignment.auth_service.user.infrastructure.UserJpaRepository;
import com.assignment.auth_service.user.infrastructure.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeUserDetailsService implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;

    public EmployeeUserDetailsService(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userJpaRepository.findByEmailIgnoreCase(username.trim().toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new EmployeeUserDetails(user);
    }

    @Transactional(readOnly = true)
    public EmployeeUserDetails loadByEmployeeId(String employeeId) {
        UserEntity user = userJpaRepository.findByEmployeeIdIgnoreCase(employeeId.trim())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new EmployeeUserDetails(user);
    }
}
