package com.slt.hospitalmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.slt.hospitalmanagement.entity.PrescriptionOrder;

public interface PrescriptionOrderRepository
        extends JpaRepository<PrescriptionOrder, Long> {

    Optional<PrescriptionOrder>
        findByMedicalRecordId(Long medicalRecordId);

    List<PrescriptionOrder>
        findAllByOrderByCreatedAtDesc();
}