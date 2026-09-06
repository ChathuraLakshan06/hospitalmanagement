package com.slt.hospitalmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.slt.hospitalmanagement.entity.LeaveRecord;

public interface LeaveRecordRepository
        extends JpaRepository<LeaveRecord, Long> {

    List<LeaveRecord>
        findAllByOrderByRequestedAtDesc();
}