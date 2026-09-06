package com.slt.hospitalmanagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.slt.hospitalmanagement.entity.Employee;
import com.slt.hospitalmanagement.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(
            EmployeeRepository employeeRepository) {

        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public List<Employee> searchEmployees(String keyword) {
        return employeeRepository.search(keyword);
    }

    public Employee getEmployeeById(Long id) {

        return employeeRepository
                .findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Employee not found"
                        )
                );
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
}