package com.example.employee_management.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nonnull
    private Long id;
    @Nonnull
    private String name;

    @OneToMany(mappedBy = "department")
    private Set<Employee> employees;

}