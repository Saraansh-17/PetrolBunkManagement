package com.assignment.hr_service.employee.infrastructure.repository;

import com.assignment.hr_service.employee.infrastructure.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeJpaRepository extends JpaRepository<EmployeeEntity, Long> {

    Optional<EmployeeEntity> findByEmailIgnoreCase(String email);

    Optional<EmployeeEntity> findByEmployeeCodeIgnoreCase(String employeeCode);

    List<EmployeeEntity> findByActiveTrue();

    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByEmployeeCodeAndIdNot(String employeeCode, Long id);
}
