package com.slt.hospitalmanagement.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.slt.hospitalmanagement.entity.LabTestStatus;
import com.slt.hospitalmanagement.entity.LaboratoryTest;
import com.slt.hospitalmanagement.repository.LaboratoryTestRepository;

@Service
public class LaboratoryTestService {

    private final LaboratoryTestRepository repository;

    public LaboratoryTestService(
            LaboratoryTestRepository repository) {

        this.repository = repository;
    }

    public List<LaboratoryTest> getAllTests() {

        return repository
                .findAllByOrderByRequestedAtDesc();
    }

    public LaboratoryTest getTestById(Long id) {

        return repository
                .findById(id)
                .orElseThrow(
                    () -> new RuntimeException(
                        "Laboratory test not found"
                    )
                );
    }

    public LaboratoryTest saveTest(
            LaboratoryTest test) {

        return repository.save(test);
    }

    public void markSampleCollected(Long id) {

        LaboratoryTest test =
                getTestById(id);

        test.setStatus(
                LabTestStatus.SAMPLE_COLLECTED
        );

        test.setSampleCollectedAt(
                LocalDateTime.now()
        );

        repository.save(test);
    }

    public void completeTest(
            Long id,
            String result,
            String referenceRange,
            String notes) {

        LaboratoryTest test =
                getTestById(id);

        test.setResult(result);

        test.setReferenceRange(
                referenceRange);

        test.setNotes(notes);

        test.setStatus(
                LabTestStatus.COMPLETED
        );

        test.setCompletedAt(
                LocalDateTime.now()
        );

        repository.save(test);
    }
}