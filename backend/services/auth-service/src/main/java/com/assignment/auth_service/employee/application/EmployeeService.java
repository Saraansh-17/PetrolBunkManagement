package com.assignment.auth_service.employee.application;

import com.assignment.auth_service.employee.application.dto.EmployeeResponse;
import com.assignment.auth_service.employee.application.dto.RegisterEmployeeRequest;
import com.assignment.auth_service.employee.application.dto.UpdateEmployeeRequest;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse register(RegisterEmployeeRequest request);

    List<EmployeeResponse> findAllEmployees();

    EmployeeResponse findById(Long id);

    EmployeeResponse update(Long id, UpdateEmployeeRequest request);

    void delete(Long id);
}
