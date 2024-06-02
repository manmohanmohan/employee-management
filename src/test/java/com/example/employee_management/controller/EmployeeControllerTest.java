package com.example.employee_management.controller;

import com.example.employee_management.dto.EmployeeDTO;
import com.example.employee_management.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEmployees() {
        // Mocking service method
        List<EmployeeDTO> mockEmployees = new ArrayList<>();
        when(employeeService.getAllEmployees()).thenReturn(mockEmployees);

        // Calling controller method
        ResponseEntity<List<EmployeeDTO>> response = employeeController.getAllEmployees();

        // Verifying service method invocation
        verify(employeeService, times(1)).getAllEmployees();

        // Asserting response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockEmployees, response.getBody());
    }

    @Test
    void testSaveEmployee() {
        // Mocking service method
        EmployeeDTO employeeDTO = new EmployeeDTO();
        doNothing().when(employeeService).saveEmployee(employeeDTO);

        // Calling controller method
        ResponseEntity<String> response = employeeController.saveEmployee(employeeDTO);

        // Verifying service method invocation
        verify(employeeService, times(1)).saveEmployee(employeeDTO);

        // Asserting response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Employee details have been saved successfully.", response.getBody());
    }

    @Test
    void testGetEmployeesByDepartment() {
        // Mocking service method
        String departmentName = "IT";
        List<EmployeeDTO> mockEmployees = new ArrayList<>();
        when(employeeService.getEmployeesByDepartment(departmentName)).thenReturn(mockEmployees);

        // Calling controller method
        ResponseEntity<List<EmployeeDTO>> response = employeeController.getEmployeesByDepartment(departmentName);

        // Verifying service method invocation
        verify(employeeService, times(1)).getEmployeesByDepartment(departmentName);

        // Asserting response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockEmployees, response.getBody());
    }

    @Test
    void testGetEmployeesBySalary() {
        // Mocking service method
        double salary = 50000;
        boolean isGreaterThan = true;
        List<EmployeeDTO> mockEmployees = new ArrayList<>();
        when(employeeService.getEmployeesBySalary(salary, isGreaterThan)).thenReturn(mockEmployees);

        // Calling controller method
        ResponseEntity<List<EmployeeDTO>> response = employeeController.getEmployeesBySalary(salary, isGreaterThan);

        // Verifying service method invocation
        verify(employeeService, times(1)).getEmployeesBySalary(salary, isGreaterThan);

        // Asserting response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockEmployees, response.getBody());
    }
}
