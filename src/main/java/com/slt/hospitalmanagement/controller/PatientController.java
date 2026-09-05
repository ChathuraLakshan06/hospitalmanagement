package com.slt.hospitalmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.slt.hospitalmanagement.entity.Patient;
import com.slt.hospitalmanagement.service.PatientService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(
            PatientService patientService) {

        this.patientService = patientService;
    }

    @GetMapping
    public String listPatients(
            @RequestParam(required = false)
            String keyword,
            Model model) {

        if (keyword != null &&
            !keyword.trim().isEmpty()) {

            model.addAttribute(
                "patients",
                patientService.searchPatients(keyword)
            );

        } else {

            model.addAttribute(
                "patients",
                patientService.getAllPatients()
            );
        }

        model.addAttribute("keyword", keyword);

        return "patients";
    }

    @GetMapping("/new")
    public String showPatientForm(Model model) {

        model.addAttribute(
            "patient",
            new Patient()
        );

        return "patient-form";
    }

    @PostMapping("/save")
    public String savePatient(
            @Valid
            @ModelAttribute("patient")
            Patient patient,

            BindingResult result) {

        if (result.hasErrors()) {
            return "patient-form";
        }

        if (patient.getId() != null) {

            Patient existingPatient =
                    patientService
                        .getPatientById(
                            patient.getId()
                        );

            existingPatient.setFirstName(
                    patient.getFirstName());

            existingPatient.setLastName(
                    patient.getLastName());

            existingPatient.setDateOfBirth(
                    patient.getDateOfBirth());

            existingPatient.setGender(
                    patient.getGender());

            existingPatient.setPhone(
                    patient.getPhone());

            existingPatient.setAddress(
                    patient.getAddress());

            existingPatient.setBloodGroup(
                    patient.getBloodGroup());

            patientService
                    .savePatient(existingPatient);

        } else {

            patientService
                    .savePatient(patient);
        }

        return "redirect:/patients";
    }

    @GetMapping("/{id}/edit")
    public String editPatient(
            @PathVariable Long id,
            Model model) {

        Patient patient =
                patientService.getPatientById(id);

        model.addAttribute(
            "patient",
            patient
        );

        return "patient-form";
    }

    @PostMapping("/{id}/delete")
    public String deletePatient(
            @PathVariable Long id) {

        patientService.deletePatient(id);

        return "redirect:/patients";
    }
}