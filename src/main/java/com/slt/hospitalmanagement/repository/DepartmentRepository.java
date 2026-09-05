package com.slt.hospitalmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.slt.hospitalmanagement.entity.Department;

public interface DepartmentRepository
        extends JpaRepository<Department, Long> {

    Optional<Department> findByName(String name);
}