package com.example.employee_management.controller;

import com.example.employee_management.dto.EmployeeDTO;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.exception.DuplicateEmployeeException;
import com.example.employee_management.exception.EmployeeNotFoundException;
import com.example.employee_management.exception.GlobalExceptionHandler;
import com.example.employee_management.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
public class EmployeeControllerTest {
    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new EmployeeController(employeeService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }


    @Test
    public void testGetEmployeesByDepartment_success() throws Exception {
        String departmentName = "IT";
        List<EmployeeDTO> employees = Arrays.asList(new EmployeeDTO(), new EmployeeDTO());

        when(employeeService.getEmployeesByDepartment(departmentName)).thenReturn(employees);

        mockMvc.perform(MockMvcRequestBuilders.get("/employee/by-department")
                        .param("departmentName", departmentName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetEmployeesByDepartment_NotFound() throws Exception {
        String departmentName = "IT";
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());

        when(employeeService.getEmployeesByDepartment(departmentName))
                .thenThrow(new EmployeeNotFoundException("No employees found in department: " + departmentName));

        mockMvc.perform(MockMvcRequestBuilders.get("/employee/by-department")
                        .param("departmentName", departmentName))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No employees found in department: " + departmentName));
    }

    @Test
    public void testGetEmployeesByGreaterThanSalary() throws Exception {
        double salary = 50000.0;
        List<EmployeeDTO> employees = Arrays.asList(new EmployeeDTO(), new EmployeeDTO());

        when(employeeService.getEmployeesBySalary(salary,true))
                .thenThrow(new EmployeeNotFoundException("No employees found earning more than: " + salary));

        mockMvc.perform(MockMvcRequestBuilders.get("/employee/by-salary")
                        .param("salary", String.valueOf(salary))
                        .param("isGreaterThan", String.valueOf(true)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No employees found earning more than: " + salary));
    }

    @Test
    public void testGetEmployeesBySalary_notFound() throws Exception {
        double salary = 50000.0;
        List<EmployeeDTO> employees = Arrays.asList(new EmployeeDTO(), new EmployeeDTO());

        when(employeeService.getEmployeesBySalary(salary,true)).thenReturn(employees);

        mockMvc.perform(MockMvcRequestBuilders.get("/employee/by-salary")
                        .param("salary", String.valueOf(salary))
                        .param("isGreaterThan", String.valueOf(true)))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }


    @Test
    public void testGetAllEmployees() throws Exception {
        List<EmployeeDTO> employees = Arrays.asList(new EmployeeDTO(), new EmployeeDTO());

        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(MockMvcRequestBuilders.get("/employee"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testSaveEmployee() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("John Doe");
        employeeDTO.setDepartment("IT");
        employeeDTO.setSalary(60000.0);

        //when(employeeService.saveEmployee(any(EmployeeDTO.class))).thenReturn(true);

        doNothing().when(employeeService).saveEmployee(any(EmployeeDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/employee/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "name": "John Doe", 
                                    "department": "IT", 
                                    "salary": 60000.0 
                                    }
                                    """))
                .andExpect(status().isCreated());
    }

    @Test
    public void testSaveEmployee_DuplicateEmployee() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("John Doe");
        employeeDTO.setDepartment("IT");
        employeeDTO.setSalary(60000.0);

        // when(employeeService.saveEmployee(any(EmployeeDTO.class))).thenThrow(new RuntimeException());

        doThrow(new DuplicateEmployeeException("Duplicate Employee"+employeeDTO.getName())).when(employeeService).saveEmployee(any(EmployeeDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/employee/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "name": "John Doe", 
                                    "department": "IT", 
                                    "salary": 60000.0 
                                    }
                                    """))
                .andExpect(status().isConflict())
                .andExpect(content().string("Duplicate Employee"+employeeDTO.getName()));
    }

    @Test
    public void testSaveEmployee_Error() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("John Doe");
        employeeDTO.setDepartment("IT");
        employeeDTO.setSalary(60000.0);

        // when(employeeService.saveEmployee(any(EmployeeDTO.class))).thenThrow(new RuntimeException());

        doThrow(new RuntimeException()).when(employeeService).saveEmployee(any(EmployeeDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/employee/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"John Doe\", \"department\": \"IT\", \"salary\": 60000.0 }"))
                .andExpect(status().isInternalServerError());
    }


}
