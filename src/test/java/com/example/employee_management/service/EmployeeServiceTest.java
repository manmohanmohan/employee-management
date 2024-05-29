package com.example.employee_management.service;

import com.example.employee_management.dto.EmployeeDTO;
import com.example.employee_management.entity.Department;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.exception.DuplicateEmployeeException;
import com.example.employee_management.exception.EmployeeNotFoundException;
import com.example.employee_management.mapper.EmployeeMapper;
import com.example.employee_management.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private EmployeeDTO employeeDTO;
    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setName("Engineering");

        employee = new Employee();
        employee.setName("John Doe");
        employee.setSalary(50000.0);
        employee.setDepartment(department);

        employeeDTO = new EmployeeDTO();
        employeeDTO.setName("John Doe");
        employeeDTO.setSalary(50000.0);
        employeeDTO.setDepartment("Engineering");
    }

    @Test
    void testGetEmployeesByDepartment() {
        when(employeeRepository.findByDepartmentName("Engineering")).thenReturn(Collections.singletonList(employee));
        when(employeeMapper.employeeToEmployeeDTO(employee)).thenReturn(employeeDTO);

        List<EmployeeDTO> employeeDTOs = employeeService.getEmployeesByDepartment("Engineering");

        assertNotNull(employeeDTOs);
        assertEquals(1, employeeDTOs.size());
        assertEquals(employeeDTO, employeeDTOs.get(0));
        verify(employeeRepository, times(1)).findByDepartmentName("Engineering");
    }

    @Test
    void testGetEmployeesByDepartmentThrowsException() {
        when(employeeRepository.findByDepartmentName("Engineering")).thenReturn(Collections.emptyList());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeesByDepartment("Engineering"));
    }

    @Test
    void testGetEmployeesBySalaryGreaterThan() {
        when(employeeRepository.findBySalaryGreaterThan(50000.0)).thenReturn(Collections.singletonList(employee));
        when(employeeMapper.employeeToEmployeeDTO(employee)).thenReturn(employeeDTO);

        List<EmployeeDTO> employeeDTOs = employeeService.getEmployeesBySalary(50000.0, true);

        assertNotNull(employeeDTOs);
        assertEquals(1, employeeDTOs.size());
        assertEquals(employeeDTO, employeeDTOs.get(0));
        verify(employeeRepository, times(1)).findBySalaryGreaterThan(50000.0);
    }

    @Test
    void testGetEmployeesBySalaryLessThan() {
        when(employeeRepository.findBySalaryLessThanEqual(50000.0)).thenReturn(Collections.singletonList(employee));
        when(employeeMapper.employeeToEmployeeDTO(employee)).thenReturn(employeeDTO);

        List<EmployeeDTO> employeeDTOs = employeeService.getEmployeesBySalary(50000.0, false);

        assertNotNull(employeeDTOs);
        assertEquals(1, employeeDTOs.size());
        assertEquals(employeeDTO, employeeDTOs.get(0));
        verify(employeeRepository, times(1)).findBySalaryLessThanEqual(50000.0);
    }

    @Test
    void testGetEmployeesBySalaryThrowsException() {
        when(employeeRepository.findBySalaryGreaterThan(50000.0)).thenReturn(Collections.emptyList());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeesBySalary(50000.0, true));
    }

    @Test
    void testGetAllEmployees() {
        when(employeeRepository.findAll()).thenReturn(Collections.singletonList(employee));
        when(employeeMapper.employeeToEmployeeDTO(employee)).thenReturn(employeeDTO);

        List<EmployeeDTO> employeeDTOs = employeeService.getAllEmployees();

        assertNotNull(employeeDTOs);
        assertEquals(1, employeeDTOs.size());
        assertEquals(employeeDTO, employeeDTOs.get(0));
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void testGetAllEmployeesThrowsException() {
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getAllEmployees());
    }

    @Test
    void testSaveEmployee() {
        when(departmentService.findByName("Engineering")).thenReturn(department);
        when(departmentService.saveDepartment(any(Department.class))).thenReturn(department);
        when(employeeMapper.employeeDTOToEmployee(employeeDTO)).thenReturn(employee);

        employeeService.saveEmployee(employeeDTO);

        ArgumentCaptor<Employee> employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(employeeArgumentCaptor.capture());
        assertEquals(employee.getName(), employeeArgumentCaptor.getValue().getName());
        assertEquals(department, employeeArgumentCaptor.getValue().getDepartment());
    }

    @Test
    void testSaveEmployeeThrowsDuplicateException() {
        when(departmentService.findByName("Engineering")).thenReturn(department);
        when(departmentService.saveDepartment(any(Department.class))).thenReturn(department);
        when(employeeRepository.findByNameAndDepartment(employeeDTO.getName(), department)).thenReturn(Optional.of(employee));

        assertThrows(DuplicateEmployeeException.class, () -> employeeService.saveEmployee(employeeDTO));
    }
}
