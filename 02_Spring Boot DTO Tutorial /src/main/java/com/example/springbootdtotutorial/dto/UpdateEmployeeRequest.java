package com.example.springbootdtotutorial.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;

/**
 * 更新员工请求DTO
 * 所有字段都是可选的，允许部分更新
 */
public record UpdateEmployeeRequest(
        String firstName,
        String lastName,
        @Email(message = "Email should be valid")
        String email,
        String department,
        @Positive(message = "Salary must be positive")
        Double salary
) {} 