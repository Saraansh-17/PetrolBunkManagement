package com.assignment.hr_service.employee.domain.repository;

import com.assignment.hr_service.employee.domain.model.Employee;

import java.util.List;
import java.util.Optional;

/**
 * Port (outbound) defined by the domain. Infrastructure provides the adapter implementation.
 */
public interface EmployeeRepository {

    Optional<Employee> findByEmailIgnoreCase(String email);

    Optional<Employee> findByEmployeeCodeIgnoreCase(String employeeCode);

    Employee save(Employee employee);

    Optional<Employee> findById(Long id);

    List<Employee> findAllActive();

    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByEmployeeCodeAndIdNot(String employeeCode, Long id);
}
