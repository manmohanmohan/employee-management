package com.example.employee_management.service;

import com.example.employee_management.dto.EmployeeDTO;
import com.example.employee_management.entity.Department;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.exception.DuplicateEmployeeException;
import com.example.employee_management.mapper.EmployeeMapper;
import com.example.employee_management.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing employees.
 */
@Service
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final DepartmentService departmentService;

    /**
     * Retrieve all employees from the database.
     *
     * @return A list of all employees as DTOs.
     */
    public List<EmployeeDTO> getAllEmployees() {
        return Optional.of(employeeRepository.findAll())
                .map(employees -> employees.stream()
                        .map(employeeMapper::employeeToEmployeeDTO)
                        .collect(Collectors.toList()))
                .orElseGet(Collections::emptyList);
    }

    /**
     * Save a new employee to the database.
     *
     * @param employeeDTO The DTO representing the employee to be saved.
     * @throws DuplicateEmployeeException if an employee with the same name already exists in the department.
     */
    @Transactional
    public void saveEmployee(EmployeeDTO employeeDTO) throws DuplicateEmployeeException {
        Department department = departmentService.findByName(employeeDTO.getDepartment());
        department = departmentService.saveDepartment(department);

        Optional<Employee> existingEmployee = employeeRepository
                .findByNameAndDepartment(employeeDTO.getName(), department);
        existingEmployee.ifPresent(emp -> {
            throw new DuplicateEmployeeException("Employee with name " + employeeDTO.getName() +
                    " already exists in department " + employeeDTO.getDepartment());
        });
        Employee employee = employeeMapper.employeeDTOToEmployee(employeeDTO);
        employee.setDepartment(department);
        employeeRepository.save(employee);
    }

    /**
     * Retrieve employees by department name.
     *
     * @param department The name of the department to filter employees.
     * @return A list of employees in the specified department as DTOs.
     */
    public List<EmployeeDTO> getEmployeesByDepartment(String department) {
        return employeeRepository.findByDepartmentName(department)
                .map(employees -> employees.stream()
                        .map(employeeMapper::employeeToEmployeeDTO)
                        .collect(Collectors.toList()))
                .orElseGet(Collections::emptyList);
    }

    /**
     * Retrieve employees by salary, optionally filtered by whether it's greater than or equal to the specified value.
     *
     * @param salary        The salary threshold for filtering employees.
     * @param isGreaterThan A flag indicating whether to filter employees with salary greater than the threshold.
     * @return A list of employees meeting the specified salary criteria as DTOs.
     */
    public List<EmployeeDTO> getEmployeesBySalary(double salary, boolean isGreaterThan) {
        List<Employee> employees = isGreaterThan ?
                employeeRepository.findBySalaryGreaterThan(salary).orElse(Collections.emptyList()) :
                employeeRepository.findBySalaryLessThanEqual(salary).orElse(Collections.emptyList());

        return employees.stream()
                .map(employeeMapper::employeeToEmployeeDTO)
                .collect(Collectors.toList());
    }

}
