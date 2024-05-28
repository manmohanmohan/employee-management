package com.example.employee_management.service;


import com.example.employee_management.entity.Department;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllDepartments() {
        List<Department> departments = Arrays.asList(
                new Department(1L, "IT", Set.of(new Employee())),
                new Department(2L, "HR", Set.of(new Employee()))
        );

        when(departmentRepository.findAll()).thenReturn(departments);

        List<Department> result = departmentService.getAllDepartments();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("IT", result.get(0).getName());
        assertEquals("HR", result.get(1).getName());

        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    public void testFindByName_DepartmentExists() {
        Department department = new Department(1L, "IT", Set.of(new Employee()));

        when(departmentRepository.findByName("IT")).thenReturn(Optional.of(department));

        Department result = departmentService.findByName("IT");

        assertNotNull(result);
        assertEquals("IT", result.getName());

        verify(departmentRepository, times(1)).findByName("IT");
    }

    @Test
    public void testFindByName_DepartmentDoesNotExist() {
        when(departmentRepository.findByName("Finance")).thenReturn(Optional.empty());

        Department result = departmentService.findByName("Finance");

        assertNotNull(result);
        assertEquals("Finance", result.getName());

        verify(departmentRepository, times(1)).findByName("Finance");
    }

    @Test
    public void testSaveDepartment() {
        Department department = new Department(1L, "Marketing", Set.of(new Employee()));

        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        Department result = departmentService.saveDepartment(department);

        assertNotNull(result);
        assertEquals("Marketing", result.getName());

        verify(departmentRepository, times(1)).save(department);
    }
}
