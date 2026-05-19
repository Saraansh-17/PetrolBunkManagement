package com.assignment.auth_service.integration.hr;

import com.assignment.auth_service.common.exception.BadRequestException;
import com.assignment.auth_service.config.HrIntegrationProperties;
import com.assignment.auth_service.employee.application.dto.RegisterEmployeeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Provisions HR employee profile when auth account is created (service-to-service).
 */
@Component
public class HrEmployeeProvisionClient {

    private static final Logger log = LoggerFactory.getLogger(HrEmployeeProvisionClient.class);

    private final WebClient hrWebClient;
    private final HrIntegrationProperties hrIntegrationProperties;

    public HrEmployeeProvisionClient(WebClient.Builder webClientBuilder, HrIntegrationProperties hrIntegrationProperties) {
        this.hrIntegrationProperties = hrIntegrationProperties;
        this.hrWebClient = webClientBuilder.baseUrl(hrIntegrationProperties.getBaseUrl()).build();
    }

    public void provisionHrProfile(RegisterEmployeeRequest request) {
        String[] nameParts = splitName(request.getName());
        Map<String, Object> body = new HashMap<>();
        body.put("employeeCode", request.getEmployeeId().trim());
        body.put("firstName", nameParts[0]);
        body.put("lastName", nameParts[1]);
        body.put("email", request.getEmail().trim().toLowerCase());
        body.put("phoneNumber", request.getPhoneNumber().trim());
        body.put("role", request.getJobRole() != null ? request.getJobRole() : "Employee");
        body.put("salary", request.getSalary() != null ? request.getSalary() : new BigDecimal("22000.00"));
        body.put("joiningDate", request.getJoiningDate() != null ? request.getJoiningDate() : LocalDate.now());

        try {
            hrWebClient.post()
                    .uri("/hr/internal/employees/provision")
                    .header("X-Internal-Gateway-Token", hrIntegrationProperties.getInternalToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            log.info("HR profile provisioned for employeeId={}", request.getEmployeeId());
        } catch (WebClientResponseException.Conflict ex) {
            throw new BadRequestException("HR profile already exists for employee id: " + request.getEmployeeId());
        } catch (WebClientResponseException ex) {
            log.error("HR provisioning failed status={} body={}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new BadRequestException("Failed to create HR employee profile: " + ex.getStatusText());
        } catch (Exception ex) {
            log.error("HR provisioning failed", ex);
            throw new BadRequestException("Failed to create HR employee profile");
        }
    }

    private static String[] splitName(String fullName) {
        String trimmed = fullName == null ? "" : fullName.trim();
        if (trimmed.isBlank()) {
            return new String[] {"Employee", "User"};
        }
        int space = trimmed.indexOf(' ');
        if (space < 0) {
            return new String[] {trimmed, "User"};
        }
        return new String[] {trimmed.substring(0, space), trimmed.substring(space + 1).trim()};
    }
}
