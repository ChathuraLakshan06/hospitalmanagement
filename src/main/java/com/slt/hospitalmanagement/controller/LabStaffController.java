package com.slt.hospitalmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.slt.hospitalmanagement.entity.LaboratoryTest;
import com.slt.hospitalmanagement.service.LaboratoryTestService;

@Controller
@RequestMapping("/lab/lab-tests")
public class LabStaffController {

    private final LaboratoryTestService laboratoryTestService;

    public LabStaffController(
            LaboratoryTestService laboratoryTestService) {

        this.laboratoryTestService =
                laboratoryTestService;
    }

    @GetMapping
    public String listTests(Model model) {

        model.addAttribute(
                "tests",
                laboratoryTestService
                    .getAllTests()
        );

        return "lab-tests";
    }

    @PostMapping("/{id}/collect")
    public String collectSample(
            @PathVariable Long id) {

        laboratoryTestService
                .markSampleCollected(id);

        return "redirect:/lab/lab-tests";
    }

    @GetMapping("/{id}/result")
    public String resultForm(
            @PathVariable Long id,
            Model model) {

        LaboratoryTest test =
                laboratoryTestService
                    .getTestById(id);

        model.addAttribute(
                "test",
                test
        );

        return "lab-result-form";
    }

    @PostMapping("/{id}/result")
    public String saveResult(

            @PathVariable Long id,

            @RequestParam String result,

            @RequestParam(required = false)
            String referenceRange,

            @RequestParam(required = false)
            String notes) {

        laboratoryTestService
                .completeTest(
                    id,
                    result,
                    referenceRange,
                    notes
                );

        return "redirect:/lab/lab-tests";
    }

    @GetMapping("/{id}/report")
    public String report(
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