package com.assignment.hr_service.attendance.presentation.controller;

import com.assignment.hr_service.common.response.ApiResponse;
import com.assignment.hr_service.attendance.application.dto.AttendanceRequestDto;
import com.assignment.hr_service.attendance.application.dto.AttendanceResponseDto;
import com.assignment.hr_service.attendance.application.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AttendanceResponseDto>> takeAttendance(@Valid @RequestBody AttendanceRequestDto request) {
        AttendanceResponseDto saved = attendanceService.takeAttendance(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Attendance recorded successfully", saved));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<ApiResponse<List<AttendanceResponseDto>>> getByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AttendanceResponseDto> list = attendanceService.findByDate(date);
        return ResponseEntity.ok(ApiResponse.success("Attendance retrieved for date", list));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AttendanceResponseDto>>> getAll() {
        List<AttendanceResponseDto> list = attendanceService.findAll();
        return ResponseEntity.ok(ApiResponse.success("All attendance records retrieved", list));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<AttendanceResponseDto>>> getByEmployee(@PathVariable Long employeeId) {
        List<AttendanceResponseDto> list = attendanceService.findByEmployeeId(employeeId);
        return ResponseEntity.ok(ApiResponse.success("Attendance retrieved for employee", list));
    }
}
