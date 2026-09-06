package com.slt.hospitalmanagement.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.slt.hospitalmanagement.entity.MedicalRecord;
import com.slt.hospitalmanagement.entity.PrescriptionOrder;
import com.slt.hospitalmanagement.entity.PrescriptionStatus;
import com.slt.hospitalmanagement.repository.PrescriptionOrderRepository;

@Service
public class PrescriptionOrderService {

    private final PrescriptionOrderRepository repository;

    public PrescriptionOrderService(
            PrescriptionOrderRepository repository) {

        this.repository = repository;
    }

    public void createIfNeeded(
            MedicalRecord medicalRecord) {

        if (medicalRecord.getPrescription() == null ||
            medicalRecord.getPrescription()
                    .trim()
                    .isEmpty()) {

            return;
        }

        if (repository
                .findByMedicalRecordId(
                    medicalRecord.getId()
                )
                .isPresent()) {

            return;
        }

        PrescriptionOrder order =
                new PrescriptionOrder();

        order.setMedicalRecord(
                medicalRecord);

        repository.save(order);
    }

    public List<PrescriptionOrder>
        getAllOrders() {

        return repository
                .findAllByOrderByCreatedAtDesc();
    }

    public PrescriptionOrder getById(Long id) {

        return repository
                .findById(id)
                .orElseThrow(
                    () -> new RuntimeException(
                        "Prescription not found"
                    )
                );
    }

    public void dispense(
            Long id,
            String username,
            String notes) {

        PrescriptionOrder order =
                getById(id);

        order.setStatus(
                PrescriptionStatus.DISPENSED);

        order.setDispensedBy(username);

        order.setDispensedAt(
                LocalDateTime.now());

        order.setPharmacyNotes(notes);

        repository.save(order);
    }
}