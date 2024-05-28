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

@RestController
@RequestMapping("/employee")
@AllArgsConstructor
@Validated
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/by-department")
    public List<EmployeeDTO> getEmployeesByDepartment(@RequestParam @NotBlank(message = "Department name cannot be empty") String departmentName) {
        return employeeService.getEmployeesByDepartment(departmentName);
    }

    @GetMapping("/by-salary")
    public List<EmployeeDTO> getEmployeesBySalary(@RequestParam double salary,@RequestParam boolean isGreaterThan) {
        return employeeService.getEmployeesBySalary(salary,isGreaterThan);
    }

    @GetMapping()
    public List<EmployeeDTO> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.saveEmployee(employeeDTO);
        return new ResponseEntity<>("Saved Employee details", HttpStatus.CREATED);
    }
}
