package com.example.employee_management.exception;

public class DuplicateEmployeeException extends RuntimeException {
    public DuplicateEmployeeException(String message) {
        super(message);
    }
}
