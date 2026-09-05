package com.slt.hospitalmanagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.slt.hospitalmanagement.entity.Department;
import com.slt.hospitalmanagement.repository.DepartmentRepository;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(
            DepartmentRepository departmentRepository) {

        this.departmentRepository =
                departmentRepository;
    }

    public List<Department> getAllDepartments() {

        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Long id) {

        return departmentRepository
                .findById(id)
                .orElseThrow(
                    () -> new RuntimeException(
                        "Department not found"
                    )
                );
    }

    public Department saveDepartment(
            Department department) {

        return departmentRepository
                .save(department);
    }
}