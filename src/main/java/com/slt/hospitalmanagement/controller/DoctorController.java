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

import com.slt.hospitalmanagement.entity.Department;
import com.slt.hospitalmanagement.entity.Doctor;
import com.slt.hospitalmanagement.service.DepartmentService;
import com.slt.hospitalmanagement.service.DoctorService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final DepartmentService departmentService;

    public DoctorController(
            DoctorService doctorService,
            DepartmentService departmentService) {

        this.doctorService = doctorService;
        this.departmentService =
                departmentService;
    }

    @GetMapping
    public String listDoctors(
            @RequestParam(required = false)
            String keyword,
            Model model) {

        if (keyword != null &&
            !keyword.trim().isEmpty()) {

            model.addAttribute(
                    "doctors",
                    doctorService
                        .searchDoctors(keyword)
            );

        } else {

            model.addAttribute(
                    "doctors",
                    doctorService
                        .getAllDoctors()
            );
        }

        model.addAttribute(
                "keyword",
                keyword
        );

        return "doctors";
    }

    @GetMapping("/new")
    public String showDoctorForm(
            Model model) {

        model.addAttribute(
                "doctor",
                new Doctor()
        );

        model.addAttribute(
                "departments",
                departmentService
                    .getAllDepartments()
        );

        return "doctor-form";
    }

    @PostMapping("/save")
    public String saveDoctor(
            @Valid
            @ModelAttribute("doctor")
            Doctor doctor,

            BindingResult result,

            @RequestParam Long departmentId,

            Model model) {

        if (result.hasErrors()) {

            model.addAttribute(
                    "departments",
                    departmentService
                        .getAllDepartments()
            );

            return "doctor-form";
        }

        Department department =
                departmentService
                    .getDepartmentById(
                        departmentId
                    );

        if (doctor.getId() != null) {

            Doctor existingDoctor =
                    doctorService
                        .getDoctorById(
                            doctor.getId()
                        );

            existingDoctor.setFirstName(
                    doctor.getFirstName());

            existingDoctor.setLastName(
                    doctor.getLastName());

            existingDoctor.setSpecialization(
                    doctor.getSpecialization());

            existingDoctor.setPhone(
                    doctor.getPhone());

            existingDoctor.setEmail(
                    doctor.getEmail());

            existingDoctor.setDepartment(
                    department);

            existingDoctor.setAvailableDay(
                    doctor.getAvailableDay());

            existingDoctor.setAvailableFrom(
                    doctor.getAvailableFrom());

            existingDoctor.setAvailableTo(
                    doctor.getAvailableTo());

            doctorService.saveDoctor(
                    existingDoctor);

        } else {

            doctor.setDepartment(department);

            doctorService.saveDoctor(
                    doctor);
        }

        return "redirect:/admin/doctors";
    }

    @GetMapping("/{id}/edit")
    public String editDoctor(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "doctor",
                doctorService
                    .getDoctorById(id)
        );

        model.addAttribute(
                "departments",
                departmentService
                    .getAllDepartments()
        );

        return "doctor-form";
    }

    @PostMapping("/{id}/delete")
    public String deleteDoctor(
            @PathVariable Long id) {

        doctorService.deleteDoctor(id);

        return "redirect:/admin/doctors";
    }
}