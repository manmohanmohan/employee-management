package com.example.employee_management.controller;

import com.example.employee_management.dto.EmployeeDTO;
import com.example.employee_management.exception.EmployeeNotFoundException;
import com.example.employee_management.exception.GlobalExceptionHandler;
import com.example.employee_management.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    // Success test cases for each endpoint

    @Test
    @DisplayName("Get all employees")
    void getAllEmployees_Success() throws Exception {
        // Mocking the service response
        when(employeeService.getAllEmployees()).thenReturn(Collections.emptyList());

        // Performing the request and asserting the response
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Save employee details")
    void saveEmployee_Success() throws Exception {
        // Mocking the service response
        doNothing().when(employeeService).saveEmployee(any(EmployeeDTO.class));

        // Performing the request and asserting the response
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "name" : "Sachin" , "department": "IT", "salary": 50000}
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Get employees by valid department name")
    void getEmployeesByDepartment_Success() throws Exception {
        // Mocking the service response
        when(employeeService.getEmployeesByDepartment(anyString())).thenReturn(Collections.emptyList());

        // Performing the request and asserting the response
        mockMvc.perform(get("/api/departments/IT/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Get employees by salary greater than: Empty ")
    void getEmployeesBySalaryGreaterThan_Success_Case1() throws Exception {
        // Creating sample EmployeeDTO objects
        EmployeeDTO employee1 = new EmployeeDTO("John Doe", "Engineering", 60000);
        EmployeeDTO employee2 = new EmployeeDTO("Jane Smith", "Marketing", 55000);
        List<EmployeeDTO> employeeList = Arrays.asList(employee1, employee2);

        // Mocking the service response to return a list of two EmployeeDTO objects
        when(employeeService.getEmployeesBySalary(anyDouble(), anyBoolean())).thenReturn(employeeList);

        // Performing the request and asserting the response
        mockMvc.perform(get("/api/employees/salary")
                        .param("salary", "50000")
                        .param("isGreaterThan", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].department").value("Engineering"))
                .andExpect(jsonPath("$[0].salary").value(60000))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"))
                .andExpect(jsonPath("$[1].department").value("Marketing"))
                .andExpect(jsonPath("$[1].salary").value(55000));
    }

    @Test
    @DisplayName("Get employees by salary greater than: Empty result")
    void getEmployeesBySalaryGreaterThan_Success_Case2() throws Exception {
        // Mocking the service response
        when(employeeService.getEmployeesBySalary(anyDouble(), anyBoolean())).thenReturn(Collections.emptyList());

        // Performing the request and asserting the response
        mockMvc.perform(get("/api/employees/salary")
                        .param("salary", "50000")
                        .param("isGreaterThan", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }




    @Test
    @DisplayName("Get employees by salary greater than")
    void getEmployeesBySalaryLessThan_Success_Case1() throws Exception {
        // Mocking the service response
        EmployeeDTO employee1 = new EmployeeDTO("John Doe", "Engineering", 60000);
        EmployeeDTO employee2 = new EmployeeDTO("Jane Smith", "Marketing", 55000);
        List<EmployeeDTO> employeeList = Arrays.asList(employee1, employee2);

        // Mocking the service response to return a list of two EmployeeDTO objects
        when(employeeService.getEmployeesBySalary(anyDouble(), anyBoolean())).thenReturn(employeeList);

        // Performing the request and asserting the response
        mockMvc.perform(get("/api/employees/salary")
                        .param("salary", "50000")
                        .param("isGreaterThan", "false"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].department").value("Engineering"))
                .andExpect(jsonPath("$[0].salary").value(60000))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"))
                .andExpect(jsonPath("$[1].department").value("Marketing"))
                .andExpect(jsonPath("$[1].salary").value(55000));

    }

    @Test
    @DisplayName("Get employees by salary greater than: Empty result")
    void getEmployeesBySalaryLessThan_Success_Case2() throws Exception {
        // Mocking the service response
        when(employeeService.getEmployeesBySalary(anyDouble(), anyBoolean())).thenReturn(Collections.emptyList());

        // Performing the request and asserting the response
        mockMvc.perform(get("/api/employees/salary")
                        .param("salary", "50000")
                        .param("isGreaterThan", "false"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getEmployeesById_Success() throws Exception {
        // Mocking the service response
        EmployeeDTO mockEmployee = new EmployeeDTO("Sachin", "IT", 50000);
        when(employeeService.getEmployeeById(anyLong())).thenReturn(mockEmployee);

        // Performing the request and asserting the response
        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Sachin"))
                .andExpect(jsonPath("$.department").value("IT"))
                .andExpect(jsonPath("$.salary").value(50000));
    }

    // Failure test cases for each endpoint

    @Test
    void saveEmployee_Failure_InvalidInput() throws Exception {
        // Performing the request with invalid input and expecting bad request status
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "name" : " " , "department": "IT", "salary": 50000}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveEmployee_Failure_InvalidJsonSyntax() throws Exception {
        // Performing the request with invalid input and expecting bad request status
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "name" :  "department": "IT", "salary": 50000}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getEmployeesByDepartment_Failure_DepartmentNameEmpty() throws Exception {
        // Performing the request with empty department name and expecting bad request status
        mockMvc.perform(get("/api/departments//employees"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getEmployeesBySalary_Failure_InvalidSalary() throws Exception {
        // Performing the request with invalid salary value and expecting bad request status
        mockMvc.perform(get("/api/employees/salary")
                        .param("salary", "invalid")
                        .param("isGreaterThan", "true"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getEmployeesById_Failure_NotFound() throws Exception {
        // Mocking the service response with EmployeeNotFoundException
        when(employeeService.getEmployeeById(anyLong())).thenThrow(new EmployeeNotFoundException("Employee not found"));

        // Performing the request and expecting not found status
        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getEmployeesById_Failure_MethodArgumentTypeMismatch() throws Exception {
        // Mocking the service response with MethodArgumentTypeMismatchException
        when(employeeService.getEmployeeById(anyLong())).thenThrow(new MethodArgumentTypeMismatchException(1L, Long.class, "id", null, null));

        // Performing the request and expecting bad request status
        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isBadRequest());
    }
}
