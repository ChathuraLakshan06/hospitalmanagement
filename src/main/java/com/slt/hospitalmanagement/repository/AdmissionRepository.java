package com.slt.hospitalmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.slt.hospitalmanagement.entity.Admission;
import com.slt.hospitalmanagement.entity.AdmissionStatus;

public interface AdmissionRepository
        extends JpaRepository<Admission, Long> {

    List<Admission>
        findAllByOrderByAdmissionDateDesc();

    List<Admission>
        findByPatientIdOrderByAdmissionDateDesc(
                Long patientId
        );

    boolean existsByRoomNumberAndBedNumberAndStatus(
            String roomNumber,
            String bedNumber,
            AdmissionStatus status
    );
}