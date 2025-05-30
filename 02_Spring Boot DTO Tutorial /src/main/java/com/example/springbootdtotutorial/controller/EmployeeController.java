package com.example.springbootdtotutorial.controller;

import com.example.springbootdtotutorial.dto.CreateEmployeeRequest;
import com.example.springbootdtotutorial.dto.EmployeeDTO;
import com.example.springbootdtotutorial.dto.UpdateEmployeeRequest;
import com.example.springbootdtotutorial.service.EmployeeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工REST控制器
 * 负责处理HTTP请求和响应，只处理Web层逻辑
 * 业务逻辑委托给Service层处理
 * 
 * 注意：Controller只接收和返回DTO对象，从不直接操作Entity
 */
@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*") // 允许跨域访问，生产环境需要配置具体域名
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * 获取所有员工
     * GET /api/employees
     */
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        logger.info("Received request to get all employees");
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    /**
     * 根据ID获取员工
     * GET /api/employees/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        logger.info("Received request to get employee with ID: {}", id);
        return employeeService.getEmployeeById(id)
                .map(employee -> ResponseEntity.ok(employee))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 创建新员工
     * POST /api/employees
     */
    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        logger.info("Received request to create employee with email: {}", request.email());
        EmployeeDTO createdEmployee = employeeService.createEmployee(request);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    /**
     * 更新员工信息
     * PUT /api/employees/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEmployeeRequest request) {
        logger.info("Received request to update employee with ID: {}", id);
        EmployeeDTO updatedEmployee = employeeService.updateEmployee(id, request);
        return ResponseEntity.ok(updatedEmployee);
    }

    /**
     * 删除员工
     * DELETE /api/employees/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        logger.info("Received request to delete employee with ID: {}", id);
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 根据邮箱获取员工
     * GET /api/employees/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<EmployeeDTO> getEmployeeByEmail(@PathVariable String email) {
        logger.info("Received request to get employee with email: {}", email);
        return employeeService.getEmployeeByEmail(email)
                .map(employee -> ResponseEntity.ok(employee))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 根据部门获取员工列表
     * GET /api/employees/department/{department}
     */
    @GetMapping("/department/{department}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByDepartment(@PathVariable String department) {
        logger.info("Received request to get employees in department: {}", department);
        List<EmployeeDTO> employees = employeeService.getEmployeesByDepartment(department);
        return ResponseEntity.ok(employees);
    }

    /**
     * 根据姓名搜索员工
     * GET /api/employees/search?name={name}
     */
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeDTO>> searchEmployeesByName(@RequestParam String name) {
        logger.info("Received request to search employees by name: {}", name);
        List<EmployeeDTO> employees = employeeService.searchEmployeesByName(name);
        return ResponseEntity.ok(employees);
    }

    /**
     * 根据薪水范围查找员工
     * GET /api/employees/salary?min={minSalary}&max={maxSalary}
     */
    @GetMapping("/salary")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesBySalaryRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        logger.info("Received request to get employees with salary between {} and {}", min, max);
        List<EmployeeDTO> employees = employeeService.getEmployeesBySalaryRange(min, max);
        return ResponseEntity.ok(employees);
    }

    /**
     * 检查邮箱是否存在
     * GET /api/employees/check-email?email={email}
     */
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        logger.info("Received request to check if email exists: {}", email);
        boolean exists = employeeService.emailExists(email);
        return ResponseEntity.ok(exists);
    }

    /**
     * 健康检查端点
     * GET /api/employees/health
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Employee service is running!");
    }
} 