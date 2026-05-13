package com.assignment.hr_service.attendance.application.service;

import com.assignment.hr_service.attendance.application.dto.AttendanceRequestDto;
import com.assignment.hr_service.attendance.application.dto.AttendanceResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {

    AttendanceResponseDto takeAttendance(AttendanceRequestDto request);

    List<AttendanceResponseDto> findByDate(LocalDate date);

    List<AttendanceResponseDto> findAll();

    List<AttendanceResponseDto> findByEmployeeId(Long employeeId);
}
