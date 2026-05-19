package com.assignment.hr_service.attendance.domain.repository;

import com.assignment.hr_service.attendance.domain.model.Attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository {

    Attendance save(Attendance attendance);

    Optional<Attendance> findByEmployeeIdAndAttendanceDate(Long employeeId, LocalDate date);

    List<Attendance> findByAttendanceDate(LocalDate date);

    List<Attendance> findAllOrderByDateDesc();

    List<Attendance> findByEmployeeIdOrderByDateDesc(Long employeeId);
}
