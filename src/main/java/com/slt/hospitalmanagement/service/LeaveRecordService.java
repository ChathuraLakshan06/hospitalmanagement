package com.slt.hospitalmanagement.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.slt.hospitalmanagement.entity.LeaveRecord;
import com.slt.hospitalmanagement.entity.LeaveStatus;
import com.slt.hospitalmanagement.repository.LeaveRecordRepository;

@Service
public class LeaveRecordService {

    private final LeaveRecordRepository repository;

    public LeaveRecordService(
            LeaveRecordRepository repository) {

        this.repository = repository;
    }

    public List<LeaveRecord> getAllLeaves() {

        return repository
                .findAllByOrderByRequestedAtDesc();
    }

    public void saveLeave(LeaveRecord leave) {

        if (leave.getStartDate() == null ||
            leave.getEndDate() == null) {

            throw new IllegalArgumentException(
                    "Leave dates are required."
            );
        }

        if (leave.getEndDate()
                .isBefore(leave.getStartDate())) {

            throw new IllegalArgumentException(
                    "End date cannot be before start date."
            );
        }

        repository.save(leave);
    }

    public LeaveRecord getById(Long id) {

        return repository
                .findById(id)
                .orElseThrow(
                    () -> new RuntimeException(
                        "Leave record not found"
                    )
                );
    }

    public void approve(
            Long id,
            String username) {

        LeaveRecord leave = getById(id);

        leave.setStatus(
                LeaveStatus.APPROVED);

        leave.setReviewedBy(username);

        leave.setReviewedAt(
                LocalDateTime.now());

        repository.save(leave);
    }

    public void reject(
            Long id,
            String username) {

        LeaveRecord leave = getById(id);

        leave.setStatus(
                LeaveStatus.REJECTED);

        leave.setReviewedBy(username);

        leave.setReviewedAt(
                LocalDateTime.now());

        repository.save(leave);
    }
}