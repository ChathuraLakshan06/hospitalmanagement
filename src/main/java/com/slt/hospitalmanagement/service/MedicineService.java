package com.slt.hospitalmanagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.slt.hospitalmanagement.entity.Medicine;
import com.slt.hospitalmanagement.repository.MedicineRepository;

@Service
public class MedicineService {

    private final MedicineRepository medicineRepository;

    public MedicineService(
            MedicineRepository medicineRepository) {

        this.medicineRepository =
                medicineRepository;
    }

    public List<Medicine> getAllMedicines() {

        return medicineRepository.findAll();
    }

    public List<Medicine> searchMedicines(
            String keyword) {

        return medicineRepository.search(keyword);
    }

    public Medicine getMedicineById(Long id) {

        return medicineRepository
                .findById(id)
                .orElseThrow(
                    () -> new RuntimeException(
                        "Medicine not found"
                    )
                );
    }

    public Medicine saveMedicine(
            Medicine medicine) {

        return medicineRepository
                .save(medicine);
    }

    public void deleteMedicine(Long id) {

        medicineRepository.deleteById(id);
    }

    public List<Medicine> getLowStockMedicines() {

        return medicineRepository
                .findAll()
                .stream()
                .filter(Medicine::isLowStock)
                .toList();
    }

    public List<Medicine> getExpiringMedicines() {

        return medicineRepository
                .findAll()
                .stream()
                .filter(Medicine::isExpiringSoon)
                .toList();
    }
}