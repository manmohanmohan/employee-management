package com.example.employee_management.controller;

import com.example.employee_management.dto.EmployeeDTO;
import com.example.employee_management.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
@RequestMapping("/api")
@AllArgsConstructor
@Validated
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * Retrieves all employees.
     *
     * @return a ResponseEntity containing a list of all employees and the HTTP status.
     */
    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    /**
     * Saves employee details.
     *
     * @param employeeDTO the details of the employee to be saved.
     * @return a ResponseEntity indicating the status of the operation.
     */
    @PostMapping("/employees")
    public ResponseEntity<String> saveEmployee(@Valid @RequestBody  EmployeeDTO employeeDTO) {
        employeeService.saveEmployee(employeeDTO);
        return new ResponseEntity<>("Employee details have been saved successfully.", HttpStatus.CREATED);
    }

    /**
     * Retrieves employees by department name.
     *
     * @param departmentName the name of the department.
     * @return a ResponseEntity containing a list of employees in the specified department and the HTTP status.
     */
    @GetMapping("/departments/{departmentName}/employees")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByDepartment(
            @PathVariable @NotBlank(message = "Department name cannot be empty") String departmentName) {
        List<EmployeeDTO> employees = employeeService.getEmployeesByDepartment(departmentName);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    /**
     * Retrieves employees by salary criteria.
     *
     * @param salary        the salary criteria.
     * @param isGreaterThan flag indicating whether to retrieve employees with salary greater than the specified value.
     * @return a ResponseEntity containing a list of employees based on the salary criteria and the HTTP status.
     */
    @GetMapping("/employees/salary")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesBySalary(
            @RequestParam(defaultValue = "0") double salary,
            @RequestParam(defaultValue = "true") boolean isGreaterThan){
            List<EmployeeDTO> employees = employeeService.getEmployeesBySalary(salary, isGreaterThan);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }


    @GetMapping("/employees/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeesById(@PathVariable long id){
        EmployeeDTO employee = employeeService.getEmployeeById(id);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }
}
