package com.assignment.hr_service.employee.application.service;

import com.assignment.hr_service.common.exception.DuplicateResourceException;
import com.assignment.hr_service.common.exception.ResourceNotFoundException;
import com.assignment.hr_service.employee.application.dto.EmployeeRequestDto;
import com.assignment.hr_service.employee.application.dto.EmployeeResponseDto;
import com.assignment.hr_service.employee.application.mapper.EmployeeDtoMapper;
import com.assignment.hr_service.employee.domain.model.Employee;
import com.assignment.hr_service.employee.domain.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implements employee workflows: validates business rules, coordinates the domain repository,
 * and maps results to DTOs for the presentation layer.
 */
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeDtoMapper employeeDtoMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeDtoMapper employeeDtoMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeDtoMapper = employeeDtoMapper;
    }

    @Override
    public EmployeeResponseDto create(EmployeeRequestDto request) {
        if (employeeRepository.existsByEmployeeCode(request.getEmployeeCode().trim())) {
            throw new DuplicateResourceException("Employee code already exists: " + request.getEmployeeCode());
        }
        Employee toSave = employeeDtoMapper.toDomainForCreate(request);
        Employee saved = employeeRepository.save(toSave);
        return employeeDtoMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> findAllActive() {
        return employeeRepository.findAllActive().stream()
                .map(employeeDtoMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDto findById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return employeeDtoMapper.toResponseDto(employee);
    }

    @Override
    public EmployeeResponseDto update(Long id, EmployeeRequestDto request) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        String newCode = request.getEmployeeCode().trim();
        if (!newCode.equals(existing.getEmployeeCode())
                && employeeRepository.existsByEmployeeCodeAndIdNot(newCode, id)) {
            throw new DuplicateResourceException("Employee code already exists: " + newCode);
        }
        employeeDtoMapper.applyUpdate(request, existing);
        Employee saved = employeeRepository.save(existing);
        return employeeDtoMapper.toResponseDto(saved);
    }

    @Override
    public void softDelete(Long id) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        if (!existing.isActive()) {
            return;
        }
        existing.setActive(false);
        employeeRepository.save(existing);
    }
}
