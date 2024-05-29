package com.example.employee_management.repository;

import com.example.employee_management.entity.Department;
import com.example.employee_management.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByDepartmentName(String departmentName);

    List<Employee> findBySalaryGreaterThan(double salary);

    List<Employee> findBySalaryLessThanEqual(double salary);

    Optional<Employee> findByNameAndDepartment(String name, Department department);


}