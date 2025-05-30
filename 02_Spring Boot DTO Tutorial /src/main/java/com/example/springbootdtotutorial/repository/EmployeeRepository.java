package com.example.springbootdtotutorial.repository;

import com.example.springbootdtotutorial.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 员工数据访问层
 * 继承JpaRepository提供基本的CRUD操作
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * 根据邮箱查找员工
     */
    Optional<Employee> findByEmail(String email);

    /**
     * 根据部门查找所有员工
     */
    List<Employee> findByDepartment(String department);

    /**
     * 根据姓名查找员工（忽略大小写）
     */
    List<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName);

    /**
     * 查找薪水在指定范围内的员工
     */
    @Query("SELECT e FROM Employee e WHERE e.salary BETWEEN :minSalary AND :maxSalary")
    List<Employee> findEmployeesBySalaryRange(
            @Param("minSalary") Double minSalary, 
            @Param("maxSalary") Double maxSalary);

    /**
     * 检查邮箱是否存在（排除指定ID）
     */
    boolean existsByEmailAndIdNot(String email, Long id);

    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 统计每个部门的员工数量
     */
    @Query("SELECT e.department, COUNT(e) FROM Employee e GROUP BY e.department")
    List<Object[]> countEmployeesByDepartment();

    /**
     * 查找高薪员工（薪水大于指定金额）
     */
    List<Employee> findBySalaryGreaterThan(Double salary);
} 