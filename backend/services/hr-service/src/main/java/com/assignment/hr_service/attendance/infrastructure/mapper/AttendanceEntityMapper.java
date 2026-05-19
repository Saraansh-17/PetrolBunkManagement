package com.assignment.hr_service.attendance.infrastructure.mapper;

import com.assignment.hr_service.attendance.domain.model.Attendance;
import com.assignment.hr_service.attendance.infrastructure.entity.AttendanceEntity;
import com.assignment.hr_service.employee.infrastructure.entity.EmployeeEntity;
import org.springframework.stereotype.Component;

@Component
public class AttendanceEntityMapper {

    public Attendance toDomain(AttendanceEntity entity) {
        if (entity == null) {
            return null;
        }
        Attendance a = new Attendance();
        a.setId(entity.getId());
        if (entity.getEmployee() != null) {
            a.setEmployeeId(entity.getEmployee().getId());
        }
        a.setAttendanceDate(entity.getAttendanceDate());
        a.setCheckInTime(entity.getCheckInTime());
        a.setCheckOutTime(entity.getCheckOutTime());
        a.setStatus(entity.getStatus());
        a.setCreatedAt(entity.getCreatedAt());
        a.setUpdatedAt(entity.getUpdatedAt());
        return a;
    }

    public AttendanceEntity toNewEntity(Attendance domain, EmployeeEntity employee) {
        AttendanceEntity entity = new AttendanceEntity();
        entity.setEmployee(employee);
        copyMutableFields(domain, entity);
        return entity;
    }

    public void copyIntoEntity(Attendance domain, AttendanceEntity entity, EmployeeEntity employee) {
        entity.setEmployee(employee);
        copyMutableFields(domain, entity);
    }

    private void copyMutableFields(Attendance domain, AttendanceEntity entity) {
        entity.setAttendanceDate(domain.getAttendanceDate());
        entity.setCheckInTime(domain.getCheckInTime());
        entity.setCheckOutTime(domain.getCheckOutTime());
        entity.setStatus(domain.getStatus());
    }
}
