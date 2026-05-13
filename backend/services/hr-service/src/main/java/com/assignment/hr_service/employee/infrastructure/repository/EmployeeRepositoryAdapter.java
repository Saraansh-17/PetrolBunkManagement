package com.assignment.hr_service.employee.infrastructure.repository;

import com.assignment.hr_service.common.exception.ResourceNotFoundException;
import com.assignment.hr_service.employee.domain.model.Employee;
import com.assignment.hr_service.employee.infrastructure.mapper.EmployeeEntityMapper;
import com.assignment.hr_service.employee.domain.repository.EmployeeRepository;
import com.assignment.hr_service.employee.infrastructure.entity.EmployeeEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter implementing the domain {@link EmployeeRepository} port using Spring Data JPA.
 * This is the outer ring mapping between persistence technology and domain aggregates.
 */
@Repository
public class EmployeeRepositoryAdapter implements EmployeeRepository {

    private final EmployeeJpaRepository employeeJpaRepository;
    private final EmployeeEntityMapper employeeEntityMapper;

    public EmployeeRepositoryAdapter(EmployeeJpaRepository employeeJpaRepository, EmployeeEntityMapper employeeEntityMapper) {
        this.employeeJpaRepository = employeeJpaRepository;
        this.employeeEntityMapper = employeeEntityMapper;
    }

    @Override
    public Employee save(Employee employee) {
        EmployeeEntity entity;
        if (employee.getId() == null) {
            entity = employeeEntityMapper.toNewEntity(employee);
        } else {
            entity = employeeJpaRepository.findById(employee.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found for update: " + employee.getId()));
            employeeEntityMapper.copyIntoEntity(employee, entity);
        }
        EmployeeEntity saved = employeeJpaRepository.save(entity);
        return employeeEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return employeeJpaRepository.findById(id).map(employeeEntityMapper::toDomain);
    }

    @Override
    public List<Employee> findAllActive() {
        return employeeJpaRepository.findByActiveTrue().stream()
                .map(employeeEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByEmployeeCode(String employeeCode) {
        return employeeJpaRepository.existsByEmployeeCode(employeeCode);
    }

    @Override
    public boolean existsByEmployeeCodeAndIdNot(String employeeCode, Long id) {
        return employeeJpaRepository.existsByEmployeeCodeAndIdNot(employeeCode, id);
    }
}
