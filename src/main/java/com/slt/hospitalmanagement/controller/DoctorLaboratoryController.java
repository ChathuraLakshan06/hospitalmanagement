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

import com.slt.hospitalmanagement.entity.LaboratoryTest;
import com.slt.hospitalmanagement.entity.Patient;
import com.slt.hospitalmanagement.service.LaboratoryTestService;
import com.slt.hospitalmanagement.service.PatientService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/doctor/lab-tests")
public class DoctorLaboratoryController {

    private final LaboratoryTestService laboratoryTestService;
    private final PatientService patientService;

    public DoctorLaboratoryController(
            LaboratoryTestService laboratoryTestService,
            PatientService patientService) {

        this.laboratoryTestService =
                laboratoryTestService;

        this.patientService =
                patientService;
    }

    @GetMapping
    public String listTests(Model model) {

        model.addAttribute(
                "tests",
                laboratoryTestService
                    .getAllTests()
        );

        return "doctor-lab-tests";
    }

    @GetMapping("/new")
    public String newTest(
            @RequestParam(required = false)
            Long patientId,
            Model model) {

        LaboratoryTest test =
                new LaboratoryTest();

        model.addAttribute(
                "laboratoryTest",
                test
        );

        model.addAttribute(
                "patients",
                patientService.getAllPatients()
        );

        model.addAttribute(
                "selectedPatientId",
                patientId
        );

        return "lab-test-request-form";
    }

    @PostMapping("/save")
    public String saveTest(

            @Valid
            @ModelAttribute("laboratoryTest")
            LaboratoryTest test,

            BindingResult result,

            @RequestParam Long patientId,

            Authentication authentication,

            Model model) {

        if (result.hasErrors()) {

            model.addAttribute(
                    "patients",
                    patientService.getAllPatients()
            );

            model.addAttribute(
                    "selectedPatientId",
                    patientId
            );

            return "lab-test-request-form";
        }

        Patient patient =
                patientService
                    .getPatientById(patientId);

        test.setPatient(patient);

        test.setRequestedBy(
                authentication.getName()
        );

        laboratoryTestService
                .saveTest(test);

        return "redirect:/doctor/lab-tests";
    }

    @GetMapping("/{id}/report")
    public String viewReport(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "test",
                laboratoryTestService
                    .getTestById(id)
        );

        return "lab-test-report";
    }
}