package com.slt.hospitalmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.slt.hospitalmanagement.entity.Bill;

public interface BillRepository
        extends JpaRepository<Bill, Long> {

    List<Bill> findAllByOrderByCreatedAtDesc();
}