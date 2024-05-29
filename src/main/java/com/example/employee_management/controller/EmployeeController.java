package com.example.employee_management.controller;

import com.example.employee_management.dto.EmployeeDTO;
import com.example.employee_management.service.EmployeeService;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing employee-related HTTP requests.
 */
@RestController
@RequestMapping("/employee")
@AllArgsConstructor
@Validated
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * Retrieves employees by department name.
     *
     * @param departmentName the name of the department.
     * @return a list of employees in the specified department.
     */
    @GetMapping("/by-department")
    public List<EmployeeDTO> getEmployeesByDepartment(@RequestParam @NotBlank(message = "Department name cannot be empty") String departmentName) {
        return employeeService.getEmployeesByDepartment(departmentName);
    }

    /**
     * Retrieves employees by salary criteria.
     *
     * @param salary        the salary criteria.
     * @param isGreaterThan flag indicating whether to retrieve employees with salary greater than the specified value.
     * @return a list of employees based on the salary criteria.
     */
    @GetMapping("/by-salary")
    public List<EmployeeDTO> getEmployeesBySalary(@RequestParam double salary, @RequestParam boolean isGreaterThan) {
        return employeeService.getEmployeesBySalary(salary, isGreaterThan);
    }

    /**
     * Retrieves all employees.
     *
     * @return a list of all employees.
     */
    @GetMapping()
    public List<EmployeeDTO> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    /**
     * Saves employee details.
     *
     * @param employeeDTO the details of the employee to be saved.
     * @return a ResponseEntity indicating the status of the operation.
     */
    @PostMapping("/save")
    public ResponseEntity<String> saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.saveEmployee(employeeDTO);
        return new ResponseEntity<>("Saved Employee details", HttpStatus.CREATED);
    }
}
