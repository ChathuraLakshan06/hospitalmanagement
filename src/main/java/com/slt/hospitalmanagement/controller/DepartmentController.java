package com.slt.hospitalmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.slt.hospitalmanagement.entity.Department;
import com.slt.hospitalmanagement.service.DepartmentService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(
            DepartmentService departmentService) {

        this.departmentService =
                departmentService;
    }

    @GetMapping
    public String listDepartments(Model model) {

        model.addAttribute(
                "departments",
                departmentService.getAllDepartments()
        );

        return "departments";
    }

    @GetMapping("/new")
    public String showDepartmentForm(
            Model model) {

        model.addAttribute(
                "department",
                new Department()
        );

        return "department-form";
    }

    @PostMapping("/save")
    public String saveDepartment(
            @Valid
            @ModelAttribute("department")
            Department department,
            BindingResult result) {

        if (result.hasErrors()) {
            return "department-form";
        }

        departmentService.saveDepartment(
                department);

        return "redirect:/admin/departments";
    }

    @GetMapping("/{id}/edit")
    public String editDepartment(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "department",
                departmentService
                    .getDepartmentById(id)
        );

        return "department-form";
    }
}