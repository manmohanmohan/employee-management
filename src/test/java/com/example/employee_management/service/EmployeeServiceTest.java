package com.example.employee_management.service;

import com.example.employee_management.dto.EmployeeDTO;
import com.example.employee_management.entity.Department;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.exception.DuplicateEmployeeException;
import com.example.employee_management.mapper.EmployeeMapper;
import com.example.employee_management.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllEmployees_shouldReturnListOfEmployeesDTO() {
        // Given
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee());
        when(employeeRepository.findAll()).thenReturn(employees);
        when(employeeMapper.employeeToEmployeeDTO(any(Employee.class))).thenReturn(new EmployeeDTO());

        // When
        List<EmployeeDTO> employeeDTOs = employeeService.getAllEmployees();

        // Then
        assertNotNull(employeeDTOs);
        assertEquals(1, employeeDTOs.size());
    }

    @Test
    void saveEmployee_shouldSaveEmployee_whenNoDuplicateExists() {
        // Given
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("John Doe");
        employeeDTO.setDepartment("IT");
        Department department = new Department();
        department.setName("IT");
        when(departmentService.findByName("IT")).thenReturn(department);
        when(employeeRepository.findByNameAndDepartment(anyString(), any(Department.class))).thenReturn(Optional.empty());
        when(employeeMapper.employeeDTOToEmployee(employeeDTO)).thenReturn(new Employee());

        // When
        assertDoesNotThrow(() -> employeeService.saveEmployee(employeeDTO));

        // Then
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void saveEmployee_shouldThrowDuplicateEmployeeException_whenDuplicateExists() {
        // Given
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("John Doe");
        employeeDTO.setDepartment("IT");

        Department department = new Department();
        department.setName("IT");

        when(departmentService.findByName("IT")).thenReturn(department);
        when(departmentService.saveDepartment(any(Department.class))).thenReturn(department);
        // Mocking the existing employee
        Employee existingEmployee = new Employee();
        existingEmployee.setDepartment(department);
        existingEmployee.setName("John Doe");
        when(employeeRepository.findByNameAndDepartment(anyString(),any(Department.class))).thenReturn(Optional.of(existingEmployee));

        // When, Then
        assertThrows(DuplicateEmployeeException.class, () -> employeeService.saveEmployee(employeeDTO));
        verify(employeeRepository, never()).save(any(Employee.class));
    }


    @Test
    void getEmployeesByDepartment_shouldReturnListOfEmployeesDTO() {
        // Given
        String departmentName = "IT";
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee());
        when(employeeRepository.findByDepartmentName(departmentName)).thenReturn(Optional.of(employees));
        when(employeeMapper.employeeToEmployeeDTO(any(Employee.class))).thenReturn(new EmployeeDTO());

        // When
        List<EmployeeDTO> employeeDTOs = employeeService.getEmployeesByDepartment(departmentName);

        // Then
        assertNotNull(employeeDTOs);
        assertEquals(1, employeeDTOs.size());
    }

    @Test
    void getEmployeesBySalary_shouldReturnListOfEmployeesDTO_whenSalaryIsGreaterThan() {
        // Given
        double salary = 50000;
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee());
        when(employeeRepository.findBySalaryGreaterThan(salary)).thenReturn(Optional.of(employees));
        when(employeeMapper.employeeToEmployeeDTO(any(Employee.class))).thenReturn(new EmployeeDTO());

        // When
        List<EmployeeDTO> employeeDTOs = employeeService.getEmployeesBySalary(salary, true);

        // Then
        assertNotNull(employeeDTOs);
        assertEquals(1, employeeDTOs.size());
    }

    @Test
    void getEmployeesBySalary_shouldReturnListOfEmployeesDTO_whenSalaryIsLessThanOrEqual() {
        // Given
        double salary = 50000;
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee());
        when(employeeRepository.findBySalaryLessThanEqual(salary)).thenReturn(Optional.of(employees));
        when(employeeMapper.employeeToEmployeeDTO(any(Employee.class))).thenReturn(new EmployeeDTO());

        // When
        List<EmployeeDTO> employeeDTOs = employeeService.getEmployeesBySalary(salary, false);

        // Then
        assertNotNull(employeeDTOs);
        assertEquals(1, employeeDTOs.size());
    }


}
