package com.example.springbootdtotutorial.service;

import com.example.springbootdtotutorial.dto.CreateEmployeeRequest;
import com.example.springbootdtotutorial.dto.EmployeeDTO;
import com.example.springbootdtotutorial.dto.UpdateEmployeeRequest;

import java.util.List;
import java.util.Optional;

/**
 * 员工服务接口
 * 定义所有员工相关的业务操作
 */
public interface EmployeeService {

    /**
     * 获取所有员工
     */
    List<EmployeeDTO> getAllEmployees();

    /**
     * 根据ID获取员工
     */
    Optional<EmployeeDTO> getEmployeeById(Long id);

    /**
     * 创建新员工
     */
    EmployeeDTO createEmployee(CreateEmployeeRequest request);

    /**
     * 更新员工信息
     */
    EmployeeDTO updateEmployee(Long id, UpdateEmployeeRequest request);

    /**
     * 删除员工
     */
    void deleteEmployee(Long id);

    /**
     * 根据邮箱查找员工
     */
    Optional<EmployeeDTO> getEmployeeByEmail(String email);

    /**
     * 根据部门获取员工列表
     */
    List<EmployeeDTO> getEmployeesByDepartment(String department);

    /**
     * 根据姓名搜索员工
     */
    List<EmployeeDTO> searchEmployeesByName(String name);

    /**
     * 根据薪水范围查找员工
     */
    List<EmployeeDTO> getEmployeesBySalaryRange(Double minSalary, Double maxSalary);

    /**
     * 检查邮箱是否存在
     */
    boolean emailExists(String email);

    /**
     * 检查邮箱是否存在（排除指定员工）
     */
    boolean emailExistsForOtherEmployee(String email, Long employeeId);
} 