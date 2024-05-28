package com.example.employee_management.service;

import com.example.employee_management.entity.Department;
import com.example.employee_management.repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department findByName(String departmentName) {
        return departmentRepository.findByName(departmentName).orElseGet(() -> {
            Department newDepartment = new Department();
            newDepartment.setName(departmentName);
            return newDepartment;
        });
    }

    public Department saveDepartment(Department department) {
        Department savedDepartment = departmentRepository.save(department);
        return savedDepartment;
    }
}