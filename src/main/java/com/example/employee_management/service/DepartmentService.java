package com.example.employee_management.service;

import com.example.employee_management.entity.Department;
import com.example.employee_management.repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing department-related operations.
 */
@Service
@AllArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    /**
     * Retrieves all departments from the repository.
     *
     * @return a list of all departments.
     */
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    /**
     * Finds a department by its name. If the department does not exist,
     * a new department with the given name is created and returned.
     *
     * @param departmentName the name of the department to find or create.
     * @return the existing or newly created department.
     */
    public Department findByName(String departmentName) {
        return departmentRepository.findByName(departmentName).orElseGet(() -> {
            Department newDepartment = new Department();
            newDepartment.setName(departmentName);
            return newDepartment;
        });
    }

    /**
     * Saves the given department to the repository.
     *
     * @param department the department to save.
     * @return the saved department.
     */
    public Department saveDepartment(Department department) {
        Department savedDepartment = departmentRepository.save(department);
        return savedDepartment;
    }
}
