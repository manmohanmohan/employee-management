package com.example.employee_management.mapper;


import com.example.employee_management.dto.EmployeeDTO;
import com.example.employee_management.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    @Mapping(target = "department", source = "department.name")
    EmployeeDTO employeeToEmployeeDTO(Employee employee);

    List<EmployeeDTO> employeesToEmployeeDTOs(List<Employee> employees);

    @Mapping(target = "department.name", source = "department")
    Employee employeeDTOToEmployee(EmployeeDTO employeeDTO);
}
