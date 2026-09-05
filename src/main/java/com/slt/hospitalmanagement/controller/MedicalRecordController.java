package com.slt.hospitalmanagement.controller;

import java.time.LocalDate;

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

import com.slt.hospitalmanagement.entity.MedicalRecord;
import com.slt.hospitalmanagement.entity.Patient;
import com.slt.hospitalmanagement.service.MedicalRecordService;
import com.slt.hospitalmanagement.service.PatientService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/doctor/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;
    private final PatientService patientService;

    public MedicalRecordController(
            MedicalRecordService medicalRecordService,
            PatientService patientService) {

        this.medicalRecordService =
                medicalRecordService;

        this.patientService =
                patientService;
    }

    @GetMapping
    public String patientList(Model model) {

        model.addAttribute(
                "patients",
                patientService.getAllPatients()
        );

        return "medical-records";
    }

    @GetMapping("/patient/{patientId}")
    public String patientHistory(
            @PathVariable Long patientId,
            Model model) {

        Patient patient =
                patientService
                    .getPatientById(patientId);

        model.addAttribute(
                "patient",
                patient
        );

        model.addAttribute(
                "records",
                medicalRecordService
                    .getRecordsByPatient(patientId)
        );

        return "medical-history";
    }

    @GetMapping("/new")
    public String newRecord(
            @RequestParam Long patientId,
            Model model) {

        Patient patient =
                patientService
                    .getPatientById(patientId);

        MedicalRecord record =
                new MedicalRecord();

        record.setVisitDate(
                LocalDate.now());

        model.addAttribute(
                "medicalRecord",
                record
        );

        model.addAttribute(
                "patient",
                patient
        );

        return "medical-record-form";
    }

    @PostMapping("/save")
    public String saveRecord(

            @Valid
            @ModelAttribute("medicalRecord")
            MedicalRecord medicalRecord,

            BindingResult result,

            @RequestParam Long patientId,

            Authentication authentication,

            Model model) {

        Patient patient =
                patientService
                    .getPatientById(patientId);

        if (result.hasErrors()) {

            model.addAttribute(
                    "patient",
                    patient
            );

            return "medical-record-form";
        }

        medicalRecord.setPatient(patient);

        medicalRecord.setRecordedBy(
                authentication.getName()
        );

        medicalRecordService
                .saveRecord(medicalRecord);

        return "redirect:/doctor/medical-records/patient/"
                + patientId;
    }
}