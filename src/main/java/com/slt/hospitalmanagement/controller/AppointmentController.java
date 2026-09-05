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

import com.slt.hospitalmanagement.entity.Appointment;
import com.slt.hospitalmanagement.entity.Doctor;
import com.slt.hospitalmanagement.entity.Patient;
import com.slt.hospitalmanagement.service.AppointmentService;
import com.slt.hospitalmanagement.service.DoctorService;
import com.slt.hospitalmanagement.service.PatientService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final DoctorService doctorService;

    public AppointmentController(
            AppointmentService appointmentService,
            PatientService patientService,
            DoctorService doctorService) {

        this.appointmentService =
                appointmentService;

        this.patientService =
                patientService;

        this.doctorService =
                doctorService;
    }

    @GetMapping
    public String listAppointments(
            Model model) {

        model.addAttribute(
                "appointments",
                appointmentService
                    .getAllAppointments()
        );

        return "appointments";
    }

    @GetMapping("/new")
    public String showAppointmentForm(
            Model model) {

        model.addAttribute(
                "appointment",
                new Appointment()
        );

        loadFormData(model);

        return "appointment-form";
    }

    @PostMapping("/save")
    public String saveAppointment(

            @Valid
            @ModelAttribute("appointment")
            Appointment appointment,

            BindingResult result,

            @RequestParam Long patientId,

            @RequestParam Long doctorId,

            Model model) {

        if (result.hasErrors()) {

            loadFormData(model);

            return "appointment-form";
        }

        Patient patient =
                patientService
                    .getPatientById(patientId);

        Doctor doctor =
                doctorService
                    .getDoctorById(doctorId);

        if (appointment.getId() != null) {

            Appointment existing =
                    appointmentService
                        .getAppointmentById(
                            appointment.getId()
                        );

            existing.setPatient(patient);
            existing.setDoctor(doctor);

            existing.setAppointmentDate(
                    appointment.getAppointmentDate());

            existing.setAppointmentTime(
                    appointment.getAppointmentTime());

            existing.setReason(
                    appointment.getReason());

            appointmentService
                    .saveAppointment(existing);

        } else {

            appointment.setPatient(patient);
            appointment.setDoctor(doctor);

            appointmentService
                    .saveAppointment(appointment);
        }

        return "redirect:/appointments";
    }

    @GetMapping("/{id}/edit")
    public String editAppointment(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "appointment",
                appointmentService
                    .getAppointmentById(id)
        );

        loadFormData(model);

        return "appointment-form";
    }

    @PostMapping("/{id}/cancel")
    public String cancelAppointment(
            @PathVariable Long id) {

        appointmentService
                .cancelAppointment(id);

        return "redirect:/appointments";
    }

    @PostMapping("/{id}/complete")
    public String completeAppointment(
            @PathVariable Long id) {

        appointmentService
                .completeAppointment(id);

        return "redirect:/appointments";
    }

    private void loadFormData(Model model) {

        model.addAttribute(
                "patients",
                patientService.getAllPatients()
        );

        model.addAttribute(
                "doctors",
                doctorService.getAllDoctors()
        );
    }
}