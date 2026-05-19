package com.assignment.hr_service.employee.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Input contract for create/update employee use cases. Validated at the presentation boundary.
 */
public class EmployeeRequestDto {

    @NotBlank
    @Size(max = 64)
    private String employeeCode;

    @NotBlank
    @Size(max = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    private String lastName;

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    @NotBlank
    @Pattern(regexp = "^[0-9+\\-\\s]{7,32}$", message = "Phone number must be 7–32 digits or common separators")
    private String phoneNumber;

    @NotBlank
    @Size(max = 100)
    private String role;

    @NotNull
    @Positive
    private BigDecimal salary;

    @NotNull
    private LocalDate joiningDate;

    public EmployeeRequestDto() {
    }

    public EmployeeRequestDto(
            String employeeCode,
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            String role,
            BigDecimal salary,
            LocalDate joiningDate) {
        this.employeeCode = employeeCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.salary = salary;
        this.joiningDate = joiningDate;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    @Override
    public String toString() {
        return "EmployeeRequestDto{"
                + "employeeCode='" + employeeCode + '\''
                + ", firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + ", email='" + email + '\''
                + ", phoneNumber='" + phoneNumber + '\''
                + ", role='" + role + '\''
                + ", salary=" + salary
                + ", joiningDate=" + joiningDate
                + '}';
    }
}
