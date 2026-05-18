package com.assignment.hr_service.security;

import com.assignment.hr_service.common.exception.ForbiddenException;
import com.assignment.hr_service.employee.domain.model.Employee;
import com.assignment.hr_service.employee.domain.repository.EmployeeRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final HttpServletRequest httpServletRequest;
    private final EmployeeRepository employeeRepository;

    public AuthorizationService(HttpServletRequest httpServletRequest, EmployeeRepository employeeRepository) {
        this.httpServletRequest = httpServletRequest;
        this.employeeRepository = employeeRepository;
    }

    public GatewayAuthUser currentUser() {
        Object attr = httpServletRequest.getAttribute(GatewayAuthConstants.REQUEST_ATTR_GATEWAY_USER);
        if (!(attr instanceof GatewayAuthUser)) {
            throw new ForbiddenException("Missing gateway user context");
        }
        return (GatewayAuthUser) attr;
    }

    public void requireAdmin() {
        if (!currentUser().isAdmin()) {
            throw new ForbiddenException("ADMIN role required");
        }
    }

    public long resolveEmployeeProfileIdForCurrentUser() {
        GatewayAuthUser user = currentUser();
        return employeeRepository.findByEmployeeCodeIgnoreCase(user.getEmployeeId())
                .map(Employee::getId)
                .orElseThrow(() -> new ForbiddenException(
                        "No HR profile linked to employee id: " + user.getEmployeeId()));
    }

    public void assertCanManageEmployees() {
        requireAdmin();
    }

    public void assertCanViewEmployee(long employeeId) {
        if (currentUser().isAdmin()) {
            return;
        }
        if (currentUser().isEmployee()) {
            long selfId = resolveEmployeeProfileIdForCurrentUser();
            if (employeeId != selfId) {
                throw new ForbiddenException("Employees may only access their own employee record");
            }
            return;
        }
        throw new ForbiddenException("Unsupported role");
    }

    public void assertCanMarkAttendance(long employeeIdInRequest) {
        if (currentUser().isAdmin()) {
            return;
        }
        long selfId = resolveEmployeeProfileIdForCurrentUser();
        if (employeeIdInRequest != selfId) {
            throw new ForbiddenException("Employees may only record attendance for themselves");
        }
    }

    public void assertAdminForAttendanceAggregates() {
        requireAdmin();
    }

    public void assertCanQueryAttendanceForEmployee(long employeeId) {
        if (currentUser().isAdmin()) {
            return;
        }
        long selfId = resolveEmployeeProfileIdForCurrentUser();
        if (employeeId != selfId) {
            throw new ForbiddenException("Employees may only query their own attendance");
        }
    }
}
