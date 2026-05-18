package com.assignment.hr_service.employee.presentation.controller;

import com.assignment.hr_service.common.response.ApiResponse;
import com.assignment.hr_service.employee.application.dto.EmployeeRequestDto;
import com.assignment.hr_service.employee.application.dto.EmployeeResponseDto;
import com.assignment.hr_service.employee.application.dto.HrEmployeeProvisionRequest;
import com.assignment.hr_service.employee.application.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Service-to-service employee provisioning (auth-service → HR). Secured by internal gateway token only.
 */
@RestController
@RequestMapping("/hr/internal/employees")
public class InternalEmployeeController {

    private final EmployeeService employeeService;

    public InternalEmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/provision")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> provision(
            @Valid @RequestBody HrEmployeeProvisionRequest request) {
        EmployeeRequestDto dto = new EmployeeRequestDto(
                request.getEmployeeCode().trim(),
                request.getFirstName().trim(),
                request.getLastName().trim(),
                request.getEmail().trim().toLowerCase(),
                request.getPhoneNumber().trim(),
                request.getRole().trim(),
                request.getSalary(),
                request.getJoiningDate());
        EmployeeResponseDto created = employeeService.provisionFromAuth(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("HR employee profile provisioned", created));
    }
}
