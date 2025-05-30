package com.example.springbootdtotutorial.config;

import com.example.springbootdtotutorial.model.Employee;
import com.example.springbootdtotutorial.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 数据初始化器
 * 在应用启动时创建示例数据，方便测试
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    private final EmployeeRepository employeeRepository;

    public DataInitializer(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (employeeRepository.count() == 0) {
            logger.info("Initializing sample data...");
            createSampleEmployees();
            logger.info("Sample data initialization completed!");
        } else {
            logger.info("Data already exists. Skipping initialization.");
        }
    }

    private void createSampleEmployees() {
        // 创建示例员工数据
        Employee[] sampleEmployees = {
            new Employee("张", "三", "zhangsan@example.com", "IT", 75000.0),
            new Employee("李", "四", "lisi@example.com", "HR", 60000.0),
            new Employee("王", "五", "wangwu@example.com", "Finance", 65000.0),
            new Employee("赵", "六", "zhaoliu@example.com", "IT", 80000.0),
            new Employee("陈", "七", "chenqi@example.com", "Marketing", 70000.0),
            new Employee("刘", "八", "liuba@example.com", "IT", 85000.0),
            new Employee("周", "九", "zhoujiu@example.com", "HR", 55000.0),
            new Employee("吴", "十", "wushi@example.com", "Finance", 72000.0),
            new Employee("郑", "十一", "zhengshiyi@example.com", "IT", 90000.0),
            new Employee("钱", "十二", "qianshier@example.com", "Marketing", 68000.0)
        };

        for (Employee employee : sampleEmployees) {
            employeeRepository.save(employee);
            logger.debug("Created employee: {} {}", employee.getFirstName(), employee.getLastName());
        }

        logger.info("Created {} sample employees", sampleEmployees.length);
    }
} 