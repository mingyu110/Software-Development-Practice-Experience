package com.example.springbootdtotutorial.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

/**
 * 员工DTO (Data Transfer Object) 使用Java Record
 * 
 * Java Record的优势：
 * 1. 不可变性 - 所有字段都是final的
 * 2. 自动生成构造函数、getter、toString、equals、hashCode
 * 3. 简洁的语法，减少样板代码
 * 4. 线程安全
 * 5. 更好的性能
 */
public record EmployeeDTO(
        Long id,
        
        @NotBlank(message = "First name is required")
        String firstName,
        
        @NotBlank(message = "Last name is required")
        String lastName,
        
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,
        
        String department,
        
        @Positive(message = "Salary must be positive")
        Double salary,
        
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    
    /**
     * 获取员工全名
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * 检查是否为高薪员工（薪水大于100000）
     */
    public boolean isHighSalaryEmployee() {
        return salary != null && salary > 100000;
    }

    /**
     * 获取员工创建天数
     */
    public long getDaysSinceCreated() {
        if (createdAt == null) return 0;
        return java.time.Duration.between(createdAt, LocalDateTime.now()).toDays();
    }
}