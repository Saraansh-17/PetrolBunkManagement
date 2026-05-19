package com.assignment.hr_service.attendance.infrastructure.repository;

import com.assignment.hr_service.attendance.domain.model.Attendance;
import com.assignment.hr_service.attendance.domain.repository.AttendanceRepository;
import com.assignment.hr_service.attendance.infrastructure.entity.AttendanceEntity;
import com.assignment.hr_service.attendance.infrastructure.mapper.AttendanceEntityMapper;
import com.assignment.hr_service.common.exception.ResourceNotFoundException;
import com.assignment.hr_service.employee.infrastructure.entity.EmployeeEntity;
import com.assignment.hr_service.employee.infrastructure.repository.EmployeeJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class AttendanceRepositoryAdapter implements AttendanceRepository {

    private final AttendanceJpaRepository attendanceJpaRepository;
    private final EmployeeJpaRepository employeeJpaRepository;
    private final AttendanceEntityMapper attendanceEntityMapper;

    public AttendanceRepositoryAdapter(
            AttendanceJpaRepository attendanceJpaRepository,
            EmployeeJpaRepository employeeJpaRepository,
            AttendanceEntityMapper attendanceEntityMapper) {
        this.attendanceJpaRepository = attendanceJpaRepository;
        this.employeeJpaRepository = employeeJpaRepository;
        this.attendanceEntityMapper = attendanceEntityMapper;
    }

    @Override
    public Attendance save(Attendance attendance) {
        EmployeeEntity employee = employeeJpaRepository.findById(attendance.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + attendance.getEmployeeId()));

        AttendanceEntity entity;
        if (attendance.getId() == null) {
            entity = attendanceEntityMapper.toNewEntity(attendance, employee);
        } else {
            entity = attendanceJpaRepository.findById(attendance.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with id: " + attendance.getId()));
            attendanceEntityMapper.copyIntoEntity(attendance, entity, employee);
        }
        AttendanceEntity saved = attendanceJpaRepository.save(entity);
        return attendanceEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<Attendance> findByEmployeeIdAndAttendanceDate(Long employeeId, LocalDate date) {
        return attendanceJpaRepository.findByEmployee_IdAndAttendanceDate(employeeId, date)
                .map(attendanceEntityMapper::toDomain);
    }

    @Override
    public List<Attendance> findByAttendanceDate(LocalDate date) {
        return attendanceJpaRepository.findByAttendanceDateOrderByIdAsc(date).stream()
                .map(attendanceEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Attendance> findAllOrderByDateDesc() {
        return attendanceJpaRepository.findAllByOrderByAttendanceDateDescIdDesc().stream()
                .map(attendanceEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Attendance> findByEmployeeIdOrderByDateDesc(Long employeeId) {
        return attendanceJpaRepository.findByEmployee_IdOrderByAttendanceDateDesc(employeeId).stream()
                .map(attendanceEntityMapper::toDomain)
                .collect(Collectors.toList());
    }
}
