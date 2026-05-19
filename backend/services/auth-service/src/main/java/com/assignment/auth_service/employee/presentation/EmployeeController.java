package com.assignment.auth_service.employee.presentation;

import com.assignment.auth_service.common.response.ApiResponse;
import com.assignment.auth_service.employee.application.EmployeeService;
import com.assignment.auth_service.employee.application.dto.EmployeeResponse;
import com.assignment.auth_service.employee.application.dto.RegisterEmployeeRequest;
import com.assignment.auth_service.employee.application.dto.UpdateEmployeeRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ADMIN-only employee account management (no self-registration; role always EMPLOYEE on create).
 */
@RestController
@RequestMapping("/auth/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<EmployeeResponse>> register(@Valid @RequestBody RegisterEmployeeRequest request) {
        EmployeeResponse created = employeeService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Employee registered successfully", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> findAll() {
        List<EmployeeResponse> employees = employeeService.findAllEmployees();
        return ResponseEntity.ok(ApiResponse.success("Employees retrieved successfully", employees));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeResponse>> findById(@PathVariable Long id) {
        EmployeeResponse employee = employeeService.findById(id);
        return ResponseEntity.ok(ApiResponse.success("Employee retrieved successfully", employee));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEmployeeRequest request) {
        EmployeeResponse updated = employeeService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Employee updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Employee deactivated successfully", null));
    }
}
