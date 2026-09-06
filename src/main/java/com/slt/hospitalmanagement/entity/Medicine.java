package com.slt.hospitalmanagement.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "medicines")
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "medicine_code",
        nullable = false,
        unique = true
    )
    private String medicineCode;

    @NotBlank(message = "Medicine name is required")
    @Column(nullable = false)
    private String name;

    private String genericName;

    private String category;

    @NotNull(message = "Unit price is required")
    @Column(precision = 10, scale = 2)
    private BigDecimal unitPrice;

    private int quantityInStock;

    private int reorderLevel;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Medicine() {
    }

    @PrePersist
    public void beforeSave() {

        if (medicineCode == null) {

            medicineCode =
                    "MED-" +
                    UUID.randomUUID()
                            .toString()
                            .substring(0, 8)
                            .toUpperCase();
        }

        createdAt = LocalDateTime.now();
    }

    @Transient
    public boolean isLowStock() {

        return quantityInStock <= reorderLevel;
    }

    @Transient
    public boolean isExpired() {

        return expiryDate != null &&
               expiryDate.isBefore(LocalDate.now());
    }

    @Transient
    public boolean isExpiringSoon() {

        if (expiryDate == null) {
            return false;
        }

        LocalDate today = LocalDate.now();

        return !expiryDate.isBefore(today)
                &&
                !expiryDate.isAfter(
                    today.plusDays(30)
                );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMedicineCode() {
        return medicineCode;
    }

    public void setMedicineCode(String medicineCode) {
        this.medicineCode = medicineCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}