package com.example.springbootdtotutorial.service.impl;

import com.example.springbootdtotutorial.dto.CreateEmployeeRequest;
import com.example.springbootdtotutorial.dto.EmployeeDTO;
import com.example.springbootdtotutorial.dto.UpdateEmployeeRequest;
import com.example.springbootdtotutorial.exception.ResourceNotFoundException;
import com.example.springbootdtotutorial.exception.DuplicateResourceException;
import com.example.springbootdtotutorial.model.Employee;
import com.example.springbootdtotutorial.repository.EmployeeRepository;
import com.example.springbootdtotutorial.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 员工服务实现类
 * 包含Entity与DTO之间的转换逻辑
 * 这是DTO模式的核心：在Service层处理转换，保持Controller的简洁
 */
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllEmployees() {
        logger.info("Fetching all employees");
        return employeeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeDTO> getEmployeeById(Long id) {
        logger.info("Fetching employee with ID: {}", id);
        return employeeRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public EmployeeDTO createEmployee(CreateEmployeeRequest request) {
        logger.info("Creating new employee with email: {}", request.email());
        
        // 检查邮箱是否已存在
        if (employeeRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already exists: " + request.email());
        }

        Employee employee = convertToEntity(request);
        Employee savedEmployee = employeeRepository.save(employee);
        
        logger.info("Successfully created employee with ID: {}", savedEmployee.getId());
        return convertToDTO(savedEmployee);
    }

    @Override
    public EmployeeDTO updateEmployee(Long id, UpdateEmployeeRequest request) {
        logger.info("Updating employee with ID: {}", id);
        
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        // 检查邮箱冲突
        if (request.email() != null && !request.email().equals(existingEmployee.getEmail())) {
            if (employeeRepository.existsByEmailAndIdNot(request.email(), id)) {
                throw new DuplicateResourceException("Email already exists: " + request.email());
            }
        }

        // 更新字段（只更新非空字段）
        updateEmployeeFields(existingEmployee, request);
        
        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        
        logger.info("Successfully updated employee with ID: {}", id);
        return convertToDTO(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        logger.info("Deleting employee with ID: {}", id);
        
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with ID: " + id);
        }

        employeeRepository.deleteById(id);
        logger.info("Successfully deleted employee with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeDTO> getEmployeeByEmail(String email) {
        logger.info("Fetching employee with email: {}", email);
        return employeeRepository.findByEmail(email)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesByDepartment(String department) {
        logger.info("Fetching employees in department: {}", department);
        return employeeRepository.findByDepartment(department).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> searchEmployeesByName(String name) {
        logger.info("Searching employees by name: {}", name);
        return employeeRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesBySalaryRange(Double minSalary, Double maxSalary) {
        logger.info("Fetching employees with salary between {} and {}", minSalary, maxSalary);
        return employeeRepository.findEmployeesBySalaryRange(minSalary, maxSalary).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return employeeRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExistsForOtherEmployee(String email, Long employeeId) {
        return employeeRepository.existsByEmailAndIdNot(email, employeeId);
    }

    /**
     * 将Employee实体转换为EmployeeDTO
     * 这是DTO模式的核心方法之一
     */
    private EmployeeDTO convertToDTO(Employee employee) {
        return new EmployeeDTO(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getDepartment(),
                employee.getSalary(),
                employee.getCreatedAt(),
                employee.getUpdatedAt()
        );
    }

    /**
     * 将CreateEmployeeRequest转换为Employee实体
     */
    private Employee convertToEntity(CreateEmployeeRequest request) {
        return new Employee(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.department(),
                request.salary()
        );
    }

    /**
     * 更新Employee实体的字段（只更新非空字段）
     * 这展示了部分更新的最佳实践
     */
    private void updateEmployeeFields(Employee employee, UpdateEmployeeRequest request) {
        if (request.firstName() != null && !request.firstName().trim().isEmpty()) {
            employee.setFirstName(request.firstName().trim());
        }
        if (request.lastName() != null && !request.lastName().trim().isEmpty()) {
            employee.setLastName(request.lastName().trim());
        }
        if (request.email() != null && !request.email().trim().isEmpty()) {
            employee.setEmail(request.email().trim().toLowerCase());
        }
        if (request.department() != null) {
            employee.setDepartment(request.department().trim());
        }
        if (request.salary() != null) {
            employee.setSalary(request.salary());
        }
    }
} 