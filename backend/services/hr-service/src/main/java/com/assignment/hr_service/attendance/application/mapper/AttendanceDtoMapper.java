package com.assignment.hr_service.attendance.application.mapper;

import com.assignment.hr_service.attendance.application.dto.AttendanceRequestDto;
import com.assignment.hr_service.attendance.application.dto.AttendanceResponseDto;
import com.assignment.hr_service.attendance.domain.model.Attendance;
import org.springframework.stereotype.Component;

@Component
public class AttendanceDtoMapper {

    public Attendance toNewDomain(AttendanceRequestDto dto) {
        Attendance a = new Attendance();
        a.setEmployeeId(dto.getEmployeeId());
        a.setAttendanceDate(dto.getAttendanceDate());
        a.setCheckInTime(dto.getCheckInTime());
        a.setCheckOutTime(dto.getCheckOutTime());
        a.setStatus(dto.getStatus());
        return a;
    }

    /**
     * Updates only time fields and status on an existing row (employee and date are immutable after creation).
     */
    public void applyUpdate(AttendanceRequestDto dto, Attendance existing) {
        existing.setCheckInTime(dto.getCheckInTime());
        existing.setCheckOutTime(dto.getCheckOutTime());
        existing.setStatus(dto.getStatus());
    }

    public AttendanceResponseDto toResponseDto(Attendance attendance) {
        if (attendance == null) {
            return null;
        }
        AttendanceResponseDto dto = new AttendanceResponseDto();
        dto.setId(attendance.getId());
        dto.setEmployeeId(attendance.getEmployeeId());
        dto.setAttendanceDate(attendance.getAttendanceDate());
        dto.setCheckInTime(attendance.getCheckInTime());
        dto.setCheckOutTime(attendance.getCheckOutTime());
        dto.setStatus(attendance.getStatus());
        dto.setCreatedAt(attendance.getCreatedAt());
        dto.setUpdatedAt(attendance.getUpdatedAt());
        return dto;
    }
}
