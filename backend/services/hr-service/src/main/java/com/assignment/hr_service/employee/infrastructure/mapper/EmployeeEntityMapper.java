package com.assignment.hr_service.employee.infrastructure.mapper;

import com.assignment.hr_service.employee.domain.model.Employee;
import com.assignment.hr_service.employee.infrastructure.entity.EmployeeEntity;
import org.springframework.stereotype.Component;

/**
 * Maps between persistence {@link EmployeeEntity} and domain {@link Employee}.
 * Lives in infrastructure so the application layer stays independent of JPA.
 */
@Component
public class EmployeeEntityMapper {

    public Employee toDomain(EmployeeEntity entity) {
        if (entity == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(entity.getId());
        employee.setEmployeeCode(entity.getEmployeeCode());
        employee.setFirstName(entity.getFirstName());
        employee.setLastName(entity.getLastName());
        employee.setEmail(entity.getEmail());
        employee.setPhoneNumber(entity.getPhoneNumber());
        employee.setRole(entity.getRole());
        employee.setSalary(entity.getSalary());
        employee.setJoiningDate(entity.getJoiningDate());
        employee.setActive(entity.isActive());
        employee.setCreatedAt(entity.getCreatedAt());
        employee.setUpdatedAt(entity.getUpdatedAt());
        return employee;
    }

    public EmployeeEntity toNewEntity(Employee domain) {
        EmployeeEntity entity = new EmployeeEntity();
        copyMutableFields(domain, entity);
        return entity;
    }

    public void copyIntoEntity(Employee domain, EmployeeEntity entity) {
        copyMutableFields(domain, entity);
    }

    private void copyMutableFields(Employee domain, EmployeeEntity entity) {
        entity.setEmployeeCode(domain.getEmployeeCode());
        entity.setFirstName(domain.getFirstName());
        entity.setLastName(domain.getLastName());
        entity.setEmail(domain.getEmail());
        entity.setPhoneNumber(domain.getPhoneNumber());
        entity.setRole(domain.getRole());
        entity.setSalary(domain.getSalary());
        entity.setJoiningDate(domain.getJoiningDate());
        entity.setActive(domain.isActive());
    }
}
