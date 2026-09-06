package com.slt.hospitalmanagement.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "invoice_number",
        nullable = false,
        unique = true
    )
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(precision = 12, scale = 2)
    private BigDecimal consultationCharge;

    @Column(precision = 12, scale = 2)
    private BigDecimal laboratoryCharge;

    @Column(precision = 12, scale = 2)
    private BigDecimal pharmacyCharge;

    @Column(precision = 12, scale = 2)
    private BigDecimal admissionCharge;

    @Column(precision = 12, scale = 2)
    private BigDecimal otherCharge;

    @Column(precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(precision = 12, scale = 2)
    private BigDecimal paidAmount;

    @Column(precision = 12, scale = 2)
    private BigDecimal balanceAmount;

    @Enumerated(EnumType.STRING)
    private BillStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Bill() {
    }

    @PrePersist
    public void beforeSave() {

        if (invoiceNumber == null) {
            invoiceNumber =
                    "INV-" +
                    UUID.randomUUID()
                            .toString()
                            .substring(0, 8)
                            .toUpperCase();
        }

        createdAt = LocalDateTime.now();

        if (paidAmount == null) {
            paidAmount = BigDecimal.ZERO;
        }

        calculateAmounts();
    }

    @PreUpdate
    public void beforeUpdate() {
        calculateAmounts();
    }

    public void calculateAmounts() {

        consultationCharge =
                valueOrZero(consultationCharge);

        laboratoryCharge =
                valueOrZero(laboratoryCharge);

        pharmacyCharge =
                valueOrZero(pharmacyCharge);

        admissionCharge =
                valueOrZero(admissionCharge);

        otherCharge =
                valueOrZero(otherCharge);

        paidAmount =
                valueOrZero(paidAmount);

        totalAmount =
                consultationCharge
                    .add(laboratoryCharge)
                    .add(pharmacyCharge)
                    .add(admissionCharge)
                    .add(otherCharge);

        balanceAmount =
                totalAmount.subtract(paidAmount);

        if (balanceAmount.compareTo(BigDecimal.ZERO) <= 0) {

            status = BillStatus.PAID;

            balanceAmount = BigDecimal.ZERO;

        } else if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {

            status = BillStatus.PARTIALLY_PAID;

        } else {

            status = BillStatus.UNPAID;
        }
    }

    private BigDecimal valueOrZero(BigDecimal value) {

        return value == null
                ? BigDecimal.ZERO
                : value;
    }

    public Long getId() {
        return id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public BigDecimal getConsultationCharge() {
        return consultationCharge;
    }

    public void setConsultationCharge(
            BigDecimal consultationCharge) {
        this.consultationCharge = consultationCharge;
    }

    public BigDecimal getLaboratoryCharge() {
        return laboratoryCharge;
    }

    public void setLaboratoryCharge(
            BigDecimal laboratoryCharge) {
        this.laboratoryCharge = laboratoryCharge;
    }

    public BigDecimal getPharmacyCharge() {
        return pharmacyCharge;
    }

    public void setPharmacyCharge(
            BigDecimal pharmacyCharge) {
        this.pharmacyCharge = pharmacyCharge;
    }

    public BigDecimal getAdmissionCharge() {
        return admissionCharge;
    }

    public void setAdmissionCharge(
            BigDecimal admissionCharge) {
        this.admissionCharge = admissionCharge;
    }

    public BigDecimal getOtherCharge() {
        return otherCharge;
    }

    public void setOtherCharge(
            BigDecimal otherCharge) {
        this.otherCharge = otherCharge;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }

    public BillStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}