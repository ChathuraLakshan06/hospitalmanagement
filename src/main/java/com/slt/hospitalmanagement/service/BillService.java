package com.slt.hospitalmanagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.slt.hospitalmanagement.entity.Bill;
import com.slt.hospitalmanagement.repository.BillRepository;

@Service
public class BillService {

    private final BillRepository billRepository;

    public BillService(
            BillRepository billRepository) {

        this.billRepository = billRepository;
    }

    public List<Bill> getAllBills() {

        return billRepository
                .findAllByOrderByCreatedAtDesc();
    }

    public Bill getBillById(Long id) {

        return billRepository
                .findById(id)
                .orElseThrow(
                    () -> new RuntimeException(
                        "Bill not found"
                    )
                );
    }

    public Bill saveBill(Bill bill) {

        bill.calculateAmounts();

        return billRepository.save(bill);
    }
}