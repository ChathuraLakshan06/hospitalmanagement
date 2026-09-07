package com.slt.hospitalmanagement.controller;

import java.time.LocalDateTime;

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

import com.slt.hospitalmanagement.entity.Admission;
import com.slt.hospitalmanagement.entity.Doctor;
import com.slt.hospitalmanagement.entity.Patient;
import com.slt.hospitalmanagement.service.AdmissionService;
import com.slt.hospitalmanagement.service.DoctorService;
import com.slt.hospitalmanagement.service.PatientService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admissions")
public class AdmissionController {

    private final AdmissionService admissionService;
    private final PatientService patientService;
    private final DoctorService doctorService;

    public AdmissionController(
            AdmissionService admissionService,
            PatientService patientService,
            DoctorService doctorService) {

        this.admissionService =
                admissionService;

        this.patientService =
                patientService;

        this.doctorService =
                doctorService;
    }

    @GetMapping
    public String admissions(Model model) {

        model.addAttribute(
                "admissions",
                admissionService
                        .getAllAdmissions()
        );

        return "admissions";
    }

    @GetMapping("/new")
    public String newAdmission(
            @RequestParam(required = false)
            Long patientId,
            Model model) {

        Admission admission =
                new Admission();

        admission.setAdmissionDate(
                LocalDateTime.now()
        );

        model.addAttribute(
                "admission",
                admission
        );

        model.addAttribute(
                "patients",
                patientService
                        .getAllPatients()
        );

        model.addAttribute(
                "doctors",
                doctorService
                        .getAllDoctors()
        );

        model.addAttribute(
                "selectedPatientId",
                patientId
        );

        return "admission-form";
    }

    @PostMapping("/save")
    public String saveAdmission(

            @Valid
            @ModelAttribute("admission")
            Admission admission,

            BindingResult result,

            @RequestParam Long patientId,

            @RequestParam Long doctorId,

            Authentication authentication,

            Model model) {

        Patient patient =
                patientService
                        .getPatientById(
                                patientId
                        );

        Doctor doctor =
                doctorService
                        .getDoctorById(
                                doctorId
                        );

        admission.setPatient(patient);
        admission.setDoctor(doctor);

        if (result.hasErrors()) {

            model.addAttribute(
                    "patients",
                    patientService
                            .getAllPatients()
            );

            model.addAttribute(
                    "doctors",
                    doctorService
                            .getAllDoctors()
            );

            return "admission-form";
        }

        admission.setCreatedBy(
                authentication.getName()
        );

        try {

            Admission savedAdmission =
                    admissionService
                            .saveAdmission(
                                    admission
                            );

            return "redirect:/admissions/"
                    + savedAdmission.getId();

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "error",
                    e.getMessage()
            );

            model.addAttribute(
                    "patients",
                    patientService
                            .getAllPatients()
            );

            model.addAttribute(
                    "doctors",
                    doctorService
                            .getAllDoctors()
            );

            return "admission-form";
        }
    }

    @GetMapping("/{id}")
    public String viewAdmission(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "admission",
                admissionService
                        .getAdmissionById(id)
        );

        return "admission-details";
    }

    @GetMapping("/{id}/discharge")
    public String dischargeForm(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "admission",
                admissionService
                        .getAdmissionById(id)
        );

        return "discharge-form";
    }

    @PostMapping("/{id}/discharge")
    public String dischargePatient(

            @PathVariable Long id,

            @RequestParam
            String dischargeSummary,

            Authentication authentication,

            Model model) {

        try {

            admissionService
                    .dischargePatient(
                            id,
                            dischargeSummary,
                            authentication.getName()
                    );

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "admission",
                    admissionService
                            .getAdmissionById(id)
            );

            model.addAttribute(
                    "error",
                    e.getMessage()
            );

            return "discharge-form";
        }

        return "redirect:/admissions/" + id;
    }
}