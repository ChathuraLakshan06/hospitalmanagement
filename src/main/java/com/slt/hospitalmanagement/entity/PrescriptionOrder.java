package com.slt.hospitalmanagement.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "prescription_orders")
public class PrescriptionOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
        name = "medical_record_id",
        nullable = false,
        unique = true
    )
    private MedicalRecord medicalRecord;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrescriptionStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "dispensed_by")
    private String dispensedBy;

    @Column(name = "dispensed_at")
    private LocalDateTime dispensedAt;

    @Column(name = "pharmacy_notes", length = 1000)
    private String pharmacyNotes;

    public PrescriptionOrder() {
    }

    @PrePersist
    public void beforeSave() {

        if (status == null) {
            status = PrescriptionStatus.PENDING;
        }

        createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(
            MedicalRecord medicalRecord) {

        this.medicalRecord = medicalRecord;
    }

    public PrescriptionStatus getStatus() {
        return status;
    }

    public void setStatus(
            PrescriptionStatus status) {

        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getDispensedBy() {
        return dispensedBy;
    }

    public void setDispensedBy(
            String dispensedBy) {

        this.dispensedBy = dispensedBy;
    }

    public LocalDateTime getDispensedAt() {
        return dispensedAt;
    }

    public void setDispensedAt(
            LocalDateTime dispensedAt) {

        this.dispensedAt = dispensedAt;
    }

    public String getPharmacyNotes() {
        return pharmacyNotes;
    }

    public void setPharmacyNotes(
            String pharmacyNotes) {

        this.pharmacyNotes = pharmacyNotes;
    }
}