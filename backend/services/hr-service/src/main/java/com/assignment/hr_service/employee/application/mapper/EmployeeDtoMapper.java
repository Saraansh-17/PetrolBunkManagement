package com.assignment.hr_service.employee.application.mapper;

import com.assignment.hr_service.employee.application.dto.EmployeeRequestDto;
import com.assignment.hr_service.employee.application.dto.EmployeeResponseDto;
import com.assignment.hr_service.employee.domain.model.Employee;
import org.springframework.stereotype.Component;

/**
 * Maps between HTTP DTOs and domain {@link Employee} — application layer only (no JPA types).
 */
@Component
public class EmployeeDtoMapper {

    public Employee toDomainForCreate(EmployeeRequestDto dto) {
        Employee employee = new Employee();
        employee.setEmployeeCode(dto.getEmployeeCode().trim());
        employee.setFirstName(dto.getFirstName().trim());
        employee.setLastName(dto.getLastName().trim());
        employee.setEmail(dto.getEmail().trim());
        employee.setPhoneNumber(dto.getPhoneNumber().trim());
        employee.setRole(dto.getRole().trim());
        employee.setSalary(dto.getSalary());
        employee.setJoiningDate(dto.getJoiningDate());
        employee.setActive(true);
        return employee;
    }

    public void applyUpdate(EmployeeRequestDto dto, Employee existing) {
        existing.setEmployeeCode(dto.getEmployeeCode().trim());
        existing.setFirstName(dto.getFirstName().trim());
        existing.setLastName(dto.getLastName().trim());
        existing.setEmail(dto.getEmail().trim());
        existing.setPhoneNumber(dto.getPhoneNumber().trim());
        existing.setRole(dto.getRole().trim());
        existing.setSalary(dto.getSalary());
        existing.setJoiningDate(dto.getJoiningDate());
    }

    public EmployeeResponseDto toResponseDto(Employee employee) {
        if (employee == null) {
            return null;
        }
        EmployeeResponseDto dto = new EmployeeResponseDto();
        dto.setId(employee.getId());
        dto.setEmployeeCode(employee.getEmployeeCode());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setPhoneNumber(employee.getPhoneNumber());
        dto.setRole(employee.getRole());
        dto.setSalary(employee.getSalary());
        dto.setJoiningDate(employee.getJoiningDate());
        dto.setActive(employee.isActive());
        dto.setCreatedAt(employee.getCreatedAt());
        dto.setUpdatedAt(employee.getUpdatedAt());
        return dto;
    }
}
