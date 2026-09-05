package com.slt.hospitalmanagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.slt.hospitalmanagement.entity.Doctor;
import com.slt.hospitalmanagement.repository.DoctorRepository;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(
            DoctorRepository doctorRepository) {

        this.doctorRepository =
                doctorRepository;
    }

    public List<Doctor> getAllDoctors() {

        return doctorRepository.findAll();
    }

    public List<Doctor> searchDoctors(
            String keyword) {

        return doctorRepository.search(keyword);
    }

    public Doctor getDoctorById(Long id) {

        return doctorRepository
                .findById(id)
                .orElseThrow(
                    () -> new RuntimeException(
                        "Doctor not found"
                    )
                );
    }

    public Doctor saveDoctor(Doctor doctor) {

        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(Long id) {

        doctorRepository.deleteById(id);
    }
}