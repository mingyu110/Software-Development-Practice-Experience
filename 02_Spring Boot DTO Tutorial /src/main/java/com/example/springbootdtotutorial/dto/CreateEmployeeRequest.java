package com.example.springbootdtotutorial.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * 创建员工请求DTO（不包含ID和时间戳）
 */
public record CreateEmployeeRequest(
        @NotBlank(message = "First name is required")
        String firstName,
        
        @NotBlank(message = "Last name is required")
        String lastName,
        
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,
        
        String department,
        
        @Positive(message = "Salary must be positive")
        Double salary
) {} 