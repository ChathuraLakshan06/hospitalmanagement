package com.slt.hospitalmanagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.slt.hospitalmanagement.entity.Patient;
import com.slt.hospitalmanagement.repository.PatientRepository;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(
            PatientRepository patientRepository) {

        this.patientRepository = patientRepository;
    }

    public List<Patient> getAllPatients() {

        return patientRepository.findAll();
    }

    public List<Patient> searchPatients(String keyword) {

        return patientRepository.search(keyword);
    }

    public Patient getPatientById(Long id) {

        return patientRepository
                .findById(id)
                .orElseThrow(
                    () -> new RuntimeException(
                        "Patient not found"
                    )
                );
    }

    public Patient savePatient(Patient patient) {

        return patientRepository.save(patient);
    }

    public void deletePatient(Long id) {

        patientRepository.deleteById(id);
    }
}