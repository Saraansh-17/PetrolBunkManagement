package com.assignment.auth_service.employee.application;

import com.assignment.auth_service.common.exception.ConflictException;
import com.assignment.auth_service.common.exception.ForbiddenException;
import com.assignment.auth_service.common.exception.ResourceNotFoundException;
import com.assignment.auth_service.employee.application.dto.EmployeeResponse;
import com.assignment.auth_service.employee.application.dto.RegisterEmployeeRequest;
import com.assignment.auth_service.employee.application.dto.UpdateEmployeeRequest;
import com.assignment.auth_service.integration.hr.HrEmployeeProvisionClient;
import com.assignment.auth_service.user.application.mapper.UserDtoMapper;
import com.assignment.auth_service.user.domain.Role;
import com.assignment.auth_service.user.infrastructure.UserJpaRepository;
import com.assignment.auth_service.user.infrastructure.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ADMIN-only employee account provisioning (credentials for login). Role is always EMPLOYEE server-side.
 */
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDtoMapper userDtoMapper;
    private final HrEmployeeProvisionClient hrEmployeeProvisionClient;

    public EmployeeServiceImpl(
            UserJpaRepository userJpaRepository,
            PasswordEncoder passwordEncoder,
            UserDtoMapper userDtoMapper,
            HrEmployeeProvisionClient hrEmployeeProvisionClient) {
        this.userJpaRepository = userJpaRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDtoMapper = userDtoMapper;
        this.hrEmployeeProvisionClient = hrEmployeeProvisionClient;
    }

    @Override
    public EmployeeResponse register(RegisterEmployeeRequest request) {
        String email = normalizeEmail(request.getEmail());
        String employeeId = request.getEmployeeId().trim();

        if (userJpaRepository.existsByEmailIgnoreCase(email)) {
            throw new ConflictException("Email already registered");
        }
        if (userJpaRepository.existsByEmployeeIdIgnoreCase(employeeId)) {
            throw new ConflictException("Employee ID already exists");
        }

        UserEntity employee = new UserEntity();
        employee.setEmployeeId(employeeId);
        employee.setName(request.getName().trim());
        employee.setEmail(email);
        employee.setPhoneNumber(request.getPhoneNumber().trim());
        employee.setPassword(passwordEncoder.encode(request.getPassword()));
        employee.setRole(Role.EMPLOYEE);
        employee.setActive(true);

        UserEntity saved = userJpaRepository.save(employee);
        hrEmployeeProvisionClient.provisionHrProfile(request);
        log.info("Employee account created employeeId={}", saved.getEmployeeId());
        return userDtoMapper.toEmployeeResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> findAllEmployees() {
        return userJpaRepository.findByRole(Role.EMPLOYEE).stream()
                .map(userDtoMapper::toEmployeeResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse findById(Long id) {
        UserEntity user = findEmployeeById(id);
        return userDtoMapper.toEmployeeResponse(user);
    }

    @Override
    public EmployeeResponse update(Long id, UpdateEmployeeRequest request) {
        UserEntity user = findEmployeeById(id);

        String email = normalizeEmail(request.getEmail());
        String employeeId = request.getEmployeeId().trim();

        if (!email.equalsIgnoreCase(user.getEmail()) && userJpaRepository.existsByEmailIgnoreCase(email)) {
            throw new ConflictException("Email already registered");
        }
        if (!employeeId.equalsIgnoreCase(user.getEmployeeId())
                && userJpaRepository.existsByEmployeeIdIgnoreCase(employeeId)) {
            throw new ConflictException("Employee ID already exists");
        }

        user.setName(request.getName().trim());
        user.setEmployeeId(employeeId);
        user.setEmail(email);
        user.setPhoneNumber(request.getPhoneNumber().trim());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return userDtoMapper.toEmployeeResponse(userJpaRepository.save(user));
    }

    @Override
    public void delete(Long id) {
        UserEntity user = findEmployeeById(id);
        user.setActive(false);
        userJpaRepository.save(user);
        log.info("Employee deactivated id={}", id);
    }

    private UserEntity findEmployeeById(Long id) {
        UserEntity user = userJpaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        if (user.getRole() != Role.EMPLOYEE) {
            throw new ForbiddenException("Only employee accounts can be managed through this API");
        }
        return user;
    }

    private static String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }
}
