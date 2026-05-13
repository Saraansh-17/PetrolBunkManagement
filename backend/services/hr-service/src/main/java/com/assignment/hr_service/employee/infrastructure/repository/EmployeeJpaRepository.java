package com.assignment.hr_service.employee.infrastructure.repository;

import com.assignment.hr_service.employee.infrastructure.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeJpaRepository extends JpaRepository<EmployeeEntity, Long> {

    List<EmployeeEntity> findByActiveTrue();

    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByEmployeeCodeAndIdNot(String employeeCode, Long id);
}
