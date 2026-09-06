package com.slt.hospitalmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.slt.hospitalmanagement.entity.Payment;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    List<Payment>
        findByBillIdOrderByPaymentDateDesc(Long billId);
}