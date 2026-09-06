package com.slt.hospitalmanagement.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.slt.hospitalmanagement.entity.Bill;
import com.slt.hospitalmanagement.entity.Payment;
import com.slt.hospitalmanagement.repository.PaymentRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BillService billService;

    public PaymentService(
            PaymentRepository paymentRepository,
            BillService billService) {

        this.paymentRepository = paymentRepository;
        this.billService = billService;
    }

    public List<Payment> getPaymentsForBill(Long billId) {

        return paymentRepository
                .findByBillIdOrderByPaymentDateDesc(billId);
    }

    public void recordPayment(
            Long billId,
            BigDecimal amount,
            String paymentMethod,
            String referenceNumber,
            String notes,
            String receivedBy) {

        Bill bill =
                billService.getBillById(billId);

        if (amount == null ||
            amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Payment amount must be greater than zero"
            );
        }

        if (amount.compareTo(
                bill.getBalanceAmount()) > 0) {

            throw new IllegalArgumentException(
                    "Payment cannot exceed outstanding balance"
            );
        }

        Payment payment =
                new Payment();

        payment.setBill(bill);
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setReferenceNumber(referenceNumber);
        payment.setNotes(notes);
        payment.setReceivedBy(receivedBy);

        paymentRepository.save(payment);

        bill.setPaidAmount(
                bill.getPaidAmount().add(amount)
        );

        billService.saveBill(bill);
    }
}