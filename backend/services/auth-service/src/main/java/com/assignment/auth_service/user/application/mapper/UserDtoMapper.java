package com.assignment.auth_service.user.application.mapper;

import com.assignment.auth_service.auth.application.dto.AuthUserResponse;
import com.assignment.auth_service.employee.application.dto.EmployeeResponse;
import com.assignment.auth_service.user.infrastructure.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper {

    public AuthUserResponse toAuthUserResponse(UserEntity user) {
        return new AuthUserResponse(
                user.getEmployeeId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name());
    }

    public EmployeeResponse toEmployeeResponse(UserEntity user) {
        EmployeeResponse dto = new EmployeeResponse();
        dto.setId(user.getId());
        dto.setEmployeeId(user.getEmployeeId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole().name());
        dto.setActive(user.isActive());
        return dto;
    }
}
