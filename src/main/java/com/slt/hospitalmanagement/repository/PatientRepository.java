package com.slt.hospitalmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.slt.hospitalmanagement.entity.Patient;

public interface PatientRepository
        extends JpaRepository<Patient, Long> {

    @Query("""
        SELECT p FROM Patient p
        WHERE LOWER(p.patientNumber)
              LIKE LOWER(CONCAT('%', :keyword, '%'))

        OR LOWER(p.firstName)
              LIKE LOWER(CONCAT('%', :keyword, '%'))

        OR LOWER(p.lastName)
              LIKE LOWER(CONCAT('%', :keyword, '%'))

        OR LOWER(p.phone)
              LIKE LOWER(CONCAT('%', :keyword, '%'))
        """)
    List<Patient> search(@Param("keyword") String keyword);
}
