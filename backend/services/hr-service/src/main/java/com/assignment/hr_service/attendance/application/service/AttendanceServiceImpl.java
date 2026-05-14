package com.assignment.hr_service.attendance.application.service;

import com.assignment.hr_service.attendance.application.dto.AttendanceRequestDto;
import com.assignment.hr_service.attendance.application.dto.AttendanceResponseDto;
import com.assignment.hr_service.attendance.application.mapper.AttendanceDtoMapper;
import com.assignment.hr_service.attendance.domain.model.Attendance;
import com.assignment.hr_service.attendance.domain.model.AttendanceStatus;
import com.assignment.hr_service.attendance.domain.repository.AttendanceRepository;
import com.assignment.hr_service.common.exception.BusinessRuleViolationException;
import com.assignment.hr_service.common.exception.ResourceNotFoundException;
import com.assignment.hr_service.employee.domain.model.Employee;
import com.assignment.hr_service.employee.domain.repository.EmployeeRepository;
import com.assignment.hr_service.security.AuthorizationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceDtoMapper attendanceDtoMapper;
    private final AuthorizationService authorizationService;

    public AttendanceServiceImpl(
            AttendanceRepository attendanceRepository,
            EmployeeRepository employeeRepository,
            AttendanceDtoMapper attendanceDtoMapper,
            AuthorizationService authorizationService) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
        this.attendanceDtoMapper = attendanceDtoMapper;
        this.authorizationService = authorizationService;
    }

    @Override
    public AttendanceResponseDto takeAttendance(AttendanceRequestDto request) {
        authorizationService.assertCanMarkAttendance(request.getEmployeeId());
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId()));
        if (!employee.isActive()) {
            throw new BusinessRuleViolationException("Cannot record attendance for inactive employee id: " + request.getEmployeeId());
        }
        validateAttendanceRules(request);

        Optional<Attendance> existing = attendanceRepository.findByEmployeeIdAndAttendanceDate(
                request.getEmployeeId(),
                request.getAttendanceDate());

        Attendance toSave;
        if (existing.isPresent()) {
            toSave = existing.get();
            attendanceDtoMapper.applyUpdate(request, toSave);
        } else {
            toSave = attendanceDtoMapper.toNewDomain(request);
        }

        Attendance saved = attendanceRepository.save(toSave);
        return attendanceDtoMapper.toResponseDto(saved);
    }

    private void validateAttendanceRules(AttendanceRequestDto request) {
        AttendanceStatus status = request.getStatus();
        if (status == AttendanceStatus.PRESENT) {
            if (request.getCheckInTime() == null || request.getCheckOutTime() == null) {
                throw new BusinessRuleViolationException("PRESENT status requires both check-in and check-out times");
            }
        }
        if (status == AttendanceStatus.HALF_DAY) {
            if (request.getCheckInTime() == null && request.getCheckOutTime() == null) {
                throw new BusinessRuleViolationException("HALF_DAY status requires at least check-in or check-out time");
            }
        }
        if (request.getCheckInTime() != null && request.getCheckOutTime() != null
                && request.getCheckOutTime().isBefore(request.getCheckInTime())) {
            throw new BusinessRuleViolationException("Check-out time must be on or after check-in time");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponseDto> findByDate(LocalDate date) {
        authorizationService.assertAdminForAttendanceAggregates();
        return attendanceRepository.findByAttendanceDate(date).stream()
                .map(attendanceDtoMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponseDto> findAll() {
        authorizationService.assertAdminForAttendanceAggregates();
        return attendanceRepository.findAllOrderByDateDesc().stream()
                .map(attendanceDtoMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponseDto> findByEmployeeId(Long employeeId) {
        authorizationService.assertCanQueryAttendanceForEmployee(employeeId);
        if (!employeeRepository.findById(employeeId).isPresent()) {
            throw new ResourceNotFoundException("Employee not found with id: " + employeeId);
        }
        return attendanceRepository.findByEmployeeIdOrderByDateDesc(employeeId).stream()
                .map(attendanceDtoMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
