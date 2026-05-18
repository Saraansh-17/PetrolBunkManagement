package com.assignment.hr_service.employee.presentation.controller;

import com.assignment.hr_service.common.response.ApiResponse;
import com.assignment.hr_service.employee.application.dto.EmployeeRequestDto;
import com.assignment.hr_service.employee.application.dto.EmployeeResponseDto;
import com.assignment.hr_service.employee.application.service.EmployeeService;
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
 * Presentation adapter: HTTP mapping and validation trigger only. Business rules live in {@link EmployeeService}.
 */
@RestController
@RequestMapping("/hr/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * HTTP POST → service create → {@link ResponseEntity} with {@link ApiResponse} envelope.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> create(@Valid @RequestBody EmployeeRequestDto request) {
        EmployeeResponseDto created = employeeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Employee created successfully", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeResponseDto>>> findAll() {
        List<EmployeeResponseDto> list = employeeService.findAllActive();
        return ResponseEntity.ok(ApiResponse.success("Employees retrieved successfully", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> findById(@PathVariable Long id) {
        EmployeeResponseDto dto = employeeService.findById(id);
        return ResponseEntity.ok(ApiResponse.success("Employee retrieved successfully", dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> update(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequestDto request) {
        EmployeeResponseDto updated = employeeService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Employee updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> softDelete(@PathVariable Long id) {
        employeeService.softDelete(id);
        return ResponseEntity.ok(ApiResponse.success("Employee deactivated successfully", null));
    }
}
