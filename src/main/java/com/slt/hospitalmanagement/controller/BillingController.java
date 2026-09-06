package com.slt.hospitalmanagement.controller;

import java.math.BigDecimal;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.slt.hospitalmanagement.entity.Bill;
import com.slt.hospitalmanagement.entity.Patient;
import com.slt.hospitalmanagement.service.BillService;
import com.slt.hospitalmanagement.service.PatientService;
import com.slt.hospitalmanagement.service.PaymentService;

@Controller
@RequestMapping("/billing")
public class BillingController {

    private final BillService billService;
    private final PatientService patientService;
    private final PaymentService paymentService;

    public BillingController(
            BillService billService,
            PatientService patientService,
            PaymentService paymentService) {

        this.billService = billService;
        this.patientService = patientService;
        this.paymentService = paymentService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute(
                "bills",
                billService.getAllBills()
        );

        return "billing-dashboard";
    }

    @GetMapping("/new")
    public String newBill(Model model) {

        model.addAttribute(
                "bill",
                new Bill()
        );

        model.addAttribute(
                "patients",
                patientService.getAllPatients()
        );

        return "bill-form";
    }

    @PostMapping("/save")
    public String saveBill(
            @ModelAttribute Bill bill,
            @RequestParam Long patientId) {

        Patient patient =
                patientService
                    .getPatientById(patientId);

        bill.setPatient(patient);

        billService.saveBill(bill);

        return "redirect:/billing/"
                + bill.getId();
    }

    @GetMapping("/{id}")
    public String viewBill(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "bill",
                billService.getBillById(id)
        );

        model.addAttribute(
                "payments",
                paymentService
                    .getPaymentsForBill(id)
        );

        return "bill-details";
    }

    @PostMapping("/{id}/payment")
    public String recordPayment(
            @PathVariable Long id,
            @RequestParam BigDecimal amount,
            @RequestParam String paymentMethod,
            @RequestParam(required = false)
            String referenceNumber,
            @RequestParam(required = false)
            String notes,
            Authentication authentication,
            Model model) {

        try {

            paymentService.recordPayment(
                    id,
                    amount,
                    paymentMethod,
                    referenceNumber,
                    notes,
                    authentication.getName()
            );

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "bill",
                    billService.getBillById(id)
            );

            model.addAttribute(
                    "payments",
                    paymentService
                        .getPaymentsForBill(id)
            );

            model.addAttribute(
                    "error",
                    e.getMessage()
            );

            return "bill-details";
        }

        return "redirect:/billing/" + id;
    }
}