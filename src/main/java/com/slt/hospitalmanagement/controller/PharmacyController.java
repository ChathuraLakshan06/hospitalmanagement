package com.slt.hospitalmanagement.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.slt.hospitalmanagement.entity.Medicine;
import com.slt.hospitalmanagement.service.MedicineService;
import com.slt.hospitalmanagement.service.PrescriptionOrderService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/pharmacy")
public class PharmacyController {

    private final MedicineService medicineService;

    private final PrescriptionOrderService
            prescriptionOrderService;

    public PharmacyController(
            MedicineService medicineService,
            PrescriptionOrderService prescriptionOrderService) {

        this.medicineService =
                medicineService;

        this.prescriptionOrderService =
                prescriptionOrderService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute(
            "lowStockCount",
            medicineService
                .getLowStockMedicines()
                .size()
        );

        model.addAttribute(
            "expiringCount",
            medicineService
                .getExpiringMedicines()
                .size()
        );

        return "pharmacy-dashboard";
    }

    @GetMapping("/medicines")
    public String medicines(
            @RequestParam(required = false)
            String keyword,
            Model model) {

        if (keyword != null &&
            !keyword.trim().isEmpty()) {

            model.addAttribute(
                "medicines",
                medicineService
                    .searchMedicines(keyword)
            );

        } else {

            model.addAttribute(
                "medicines",
                medicineService
                    .getAllMedicines()
            );
        }

        model.addAttribute(
            "keyword",
            keyword
        );

        return "medicines";
    }

    @GetMapping("/medicines/new")
    public String newMedicine(Model model) {

        model.addAttribute(
            "medicine",
            new Medicine()
        );

        return "medicine-form";
    }

    @GetMapping("/medicines/{id}/edit")
    public String editMedicine(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
            "medicine",
            medicineService
                .getMedicineById(id)
        );

        return "medicine-form";
    }

    @PostMapping("/medicines/save")
    public String saveMedicine(

            @Valid
            @ModelAttribute("medicine")
            Medicine medicine,

            BindingResult result) {

        if (result.hasErrors()) {
            return "medicine-form";
        }

        if (medicine.getId() != null) {

            Medicine existing =
                    medicineService
                        .getMedicineById(
                            medicine.getId()
                        );

            existing.setName(
                    medicine.getName());

            existing.setGenericName(
                    medicine.getGenericName());

            existing.setCategory(
                    medicine.getCategory());

            existing.setUnitPrice(
                    medicine.getUnitPrice());

            existing.setQuantityInStock(
                    medicine.getQuantityInStock());

            existing.setReorderLevel(
                    medicine.getReorderLevel());

            existing.setExpiryDate(
                    medicine.getExpiryDate());

            medicineService
                    .saveMedicine(existing);

        } else {

            medicineService
                    .saveMedicine(medicine);
        }

        return "redirect:/pharmacy/medicines";
    }

    @PostMapping("/medicines/{id}/delete")
    public String deleteMedicine(
            @PathVariable Long id) {

        medicineService
                .deleteMedicine(id);

        return "redirect:/pharmacy/medicines";
    }

    @GetMapping("/prescriptions")
    public String prescriptions(Model model) {

        model.addAttribute(
            "orders",
            prescriptionOrderService
                .getAllOrders()
        );

        return "pharmacy-prescriptions";
    }

    @GetMapping("/prescriptions/{id}/dispense")
    public String dispenseForm(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
            "order",
            prescriptionOrderService
                .getById(id)
        );

        return "prescription-dispense-form";
    }

    @PostMapping("/prescriptions/{id}/dispense")
    public String dispense(

            @PathVariable Long id,

            @RequestParam(required = false)
            String notes,

            Authentication authentication) {

        prescriptionOrderService.dispense(
                id,
                authentication.getName(),
                notes
        );

        return "redirect:/pharmacy/prescriptions";
    }
}