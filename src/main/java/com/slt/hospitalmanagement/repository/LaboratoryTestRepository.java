package com.slt.hospitalmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.slt.hospitalmanagement.entity.LaboratoryTest;

public interface LaboratoryTestRepository
        extends JpaRepository<LaboratoryTest, Long> {

    List<LaboratoryTest>
        findAllByOrderByRequestedAtDesc();

    List<LaboratoryTest>
        findByPatientIdOrderByRequestedAtDesc(
                Long patientId
        );
}