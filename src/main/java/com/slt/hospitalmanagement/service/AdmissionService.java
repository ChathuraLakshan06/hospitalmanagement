package com.slt.hospitalmanagement.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.slt.hospitalmanagement.entity.Admission;
import com.slt.hospitalmanagement.entity.AdmissionStatus;
import com.slt.hospitalmanagement.repository.AdmissionRepository;

@Service
public class AdmissionService {

    private final AdmissionRepository admissionRepository;

    public AdmissionService(
            AdmissionRepository admissionRepository) {

        this.admissionRepository =
                admissionRepository;
    }

    public List<Admission> getAllAdmissions() {

        return admissionRepository
                .findAllByOrderByAdmissionDateDesc();
    }

    public Admission getAdmissionById(Long id) {

        return admissionRepository
                .findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Admission not found"
                        )
                );
    }

    public List<Admission> getPatientAdmissions(
            Long patientId) {

        return admissionRepository
                .findByPatientIdOrderByAdmissionDateDesc(
                        patientId
                );
    }

    public Admission saveAdmission(
            Admission admission) {

        boolean bedOccupied =
                admissionRepository
                        .existsByRoomNumberAndBedNumberAndStatus(
                                admission.getRoomNumber(),
                                admission.getBedNumber(),
                                AdmissionStatus.ADMITTED
                        );

        if (bedOccupied) {

            throw new IllegalArgumentException(
                    "This room and bed are currently occupied."
            );
        }

        admission.setStatus(
                AdmissionStatus.ADMITTED
        );

        return admissionRepository
                .save(admission);
    }

    public void dischargePatient(
            Long admissionId,
            String dischargeSummary,
            String username) {

        Admission admission =
                getAdmissionById(admissionId);

        if (admission.getStatus()
                == AdmissionStatus.DISCHARGED) {

            throw new IllegalArgumentException(
                    "Patient has already been discharged."
            );
        }

        admission.setStatus(
                AdmissionStatus.DISCHARGED
        );

        admission.setDischargeDate(
                LocalDateTime.now()
        );

        admission.setDischargeSummary(
                dischargeSummary
        );

        admission.setDischargedBy(
                username
        );

        admissionRepository.save(admission);
    }
}