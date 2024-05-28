package com.example.employee_management.service;

import com.example.employee_management.dto.EmployeeDTO;
import com.example.employee_management.entity.Department;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.exception.DuplicateEmployeeException;
import com.example.employee_management.exception.EmployeeNotFoundException;
import com.example.employee_management.mapper.EmployeeMapper;
import com.example.employee_management.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final DepartmentService departmentService;

    public List<EmployeeDTO> getEmployeesByDepartment(String department) {
        List<Employee> employees = employeeRepository.findByDepartmentName(department);
        return Optional.ofNullable(employees)
                .filter(empList -> !empList.isEmpty())
                .orElseThrow(() -> new EmployeeNotFoundException("No employees found in department: " + department))
                .stream()
                .map(employeeMapper::employeeToEmployeeDTO)
                .collect(Collectors.toList());
    }

    public List<EmployeeDTO> getEmployeesBySalary(Double salary, boolean isGreaterThan) {
        List<Employee> employees = isGreaterThan ?
                employeeRepository.findBySalaryGreaterThan(salary) :
                employeeRepository.findBySalaryLessThan(salary);

        return Optional.ofNullable(employees)
                .filter(empList -> !empList.isEmpty())
                .orElseThrow(() -> new EmployeeNotFoundException((isGreaterThan ? "No employees found earning more than: " : "No employees found earning less than: ") + salary))
                .stream()
                .map(employeeMapper::employeeToEmployeeDTO)
                .collect(Collectors.toList());
    }

    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return Optional.ofNullable(employees)
                .filter(empList -> !empList.isEmpty())
                .orElseThrow(() -> new EmployeeNotFoundException("No Employees found"))
                .stream()
                .map(employeeMapper::employeeToEmployeeDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveEmployee(EmployeeDTO employeeDTO) {
        Department department = departmentService.findByName(employeeDTO.getDepartment());
        department = departmentService.saveDepartment(department);

        Optional<Employee> existingEmployee = employeeRepository.findByNameAndDepartment(employeeDTO.getName(), department);
        existingEmployee.ifPresent(emp -> {
            throw new DuplicateEmployeeException("Employee with name " + employeeDTO.getName() + " already exists in department " + employeeDTO.getDepartment());
        });
        Employee employee = employeeMapper.employeeDTOToEmployee(employeeDTO);
        employee.setDepartment(department);
        employeeRepository.save(employee);
    }
}
