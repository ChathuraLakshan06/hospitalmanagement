package com.slt.hospitalmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.slt.hospitalmanagement.entity.Employee;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {

    @Query("""
        SELECT e FROM Employee e
        WHERE LOWER(e.employeeNumber)
            LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(e.firstName)
            LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(e.lastName)
            LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(e.position)
            LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(e.department.name)
            LIKE LOWER(CONCAT('%', :keyword, '%'))
        """)
    List<Employee> search(
            @Param("keyword") String keyword
    );
}