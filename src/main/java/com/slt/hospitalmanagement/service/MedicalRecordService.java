package com.slt.hospitalmanagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.slt.hospitalmanagement.entity.MedicalRecord;
import com.slt.hospitalmanagement.repository.MedicalRecordRepository;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordService(
            MedicalRecordRepository medicalRecordRepository) {

        this.medicalRecordRepository =
                medicalRecordRepository;
    }

    public List<MedicalRecord> getRecordsByPatient(
            Long patientId) {

        return medicalRecordRepository
                .findByPatientIdOrderByVisitDateDescCreatedAtDesc(
                        patientId);
    }

    public MedicalRecord saveRecord(
            MedicalRecord medicalRecord) {

        return medicalRecordRepository
                .save(medicalRecord);
    }

    public MedicalRecord getRecordById(Long id) {

        return medicalRecordRepository
                .findById(id)
                .orElseThrow(
                    () -> new RuntimeException(
                        "Medical record not found"
                    )
                );
    }
}