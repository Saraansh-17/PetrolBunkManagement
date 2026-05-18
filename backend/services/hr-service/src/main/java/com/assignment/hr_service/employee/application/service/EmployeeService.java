package com.assignment.hr_service.employee.application.service;

import com.assignment.hr_service.employee.application.dto.EmployeeRequestDto;
import com.assignment.hr_service.employee.application.dto.EmployeeResponseDto;

import java.util.List;

/**
 * Application service interface for employee use cases (inbound port).
 */
public interface EmployeeService {

    EmployeeResponseDto create(EmployeeRequestDto request);

    /**
     * Called by auth-service during onboarding; skips user authorization (internal token only).
     */
    EmployeeResponseDto provisionFromAuth(EmployeeRequestDto request);

    List<EmployeeResponseDto> findAllActive();

    EmployeeResponseDto findById(Long id);

    EmployeeResponseDto update(Long id, EmployeeRequestDto request);

    void softDelete(Long id);
}
