package com.slt.hospitalmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.slt.hospitalmanagement.entity.Medicine;

public interface MedicineRepository
        extends JpaRepository<Medicine, Long> {

    @Query("""
        SELECT m FROM Medicine m
        WHERE LOWER(m.medicineCode)
            LIKE LOWER(CONCAT('%', :keyword, '%'))

        OR LOWER(m.name)
            LIKE LOWER(CONCAT('%', :keyword, '%'))

        OR LOWER(m.genericName)
            LIKE LOWER(CONCAT('%', :keyword, '%'))

        OR LOWER(m.category)
            LIKE LOWER(CONCAT('%', :keyword, '%'))
        """)
    List<Medicine> search(
            @Param("keyword") String keyword
    );
}