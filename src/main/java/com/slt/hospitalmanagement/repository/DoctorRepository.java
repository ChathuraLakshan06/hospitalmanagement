package com.slt.hospitalmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.slt.hospitalmanagement.entity.Doctor;

public interface DoctorRepository
        extends JpaRepository<Doctor, Long> {

    @Query("""
        SELECT d FROM Doctor d
        WHERE LOWER(d.doctorNumber)
            LIKE LOWER(CONCAT('%', :keyword, '%'))

        OR LOWER(d.firstName)
            LIKE LOWER(CONCAT('%', :keyword, '%'))

        OR LOWER(d.lastName)
            LIKE LOWER(CONCAT('%', :keyword, '%'))

        OR LOWER(d.specialization)
            LIKE LOWER(CONCAT('%', :keyword, '%'))

        OR LOWER(d.department.name)
            LIKE LOWER(CONCAT('%', :keyword, '%'))
        """)
    List<Doctor> search(
            @Param("keyword") String keyword);
}