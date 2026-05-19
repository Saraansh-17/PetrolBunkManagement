package com.assignment.hr_service.attendance.infrastructure.repository;

import com.assignment.hr_service.attendance.infrastructure.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceJpaRepository extends JpaRepository<AttendanceEntity, Long> {

    Optional<AttendanceEntity> findByEmployee_IdAndAttendanceDate(Long employeeId, LocalDate attendanceDate);

    List<AttendanceEntity> findByAttendanceDateOrderByIdAsc(LocalDate attendanceDate);

    List<AttendanceEntity> findAllByOrderByAttendanceDateDescIdDesc();

    List<AttendanceEntity> findByEmployee_IdOrderByAttendanceDateDesc(Long employeeId);
}
