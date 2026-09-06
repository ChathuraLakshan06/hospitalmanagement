package com.slt.hospitalmanagement.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {

        boolean isAdmin = authentication
                .getAuthorities()
                .stream()
                .anyMatch(a ->
                        a.getAuthority()
                                .equals("ROLE_ADMIN"));

        boolean isDoctor = authentication
                .getAuthorities()
                .stream()
                .anyMatch(a ->
                        a.getAuthority()
                                .equals("ROLE_DOCTOR"));

        boolean isReceptionist = authentication
                .getAuthorities()
                .stream()
                .anyMatch(a ->
                        a.getAuthority()
                                .equals("ROLE_RECEPTIONIST"));

        boolean isLabStaff = authentication
                .getAuthorities()
                .stream()
                .anyMatch(a ->
                        a.getAuthority()
                                .equals("ROLE_LAB_STAFF"));
        boolean isPharmacist = authentication
                .getAuthorities()
                .stream()
                .anyMatch(a ->
                        a.getAuthority()
                                .equals("ROLE_PHARMACIST"));

        if (isAdmin) {
            return "redirect:/admin/dashboard";
        }

        if (isDoctor) {
            return "redirect:/doctor/dashboard";
        }

        if (isReceptionist) {
            return "redirect:/reception/dashboard";
        }

        if (isLabStaff) {
            return "redirect:/lab/dashboard";
        }
        if (isPharmacist) {
            return "redirect:/pharmacy/dashboard";
        }

        return "access-denied";
    }


    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin-dashboard";
    }


    @GetMapping("/doctor/dashboard")
    public String doctorDashboard() {
        return "doctor-dashboard";
    }


    @GetMapping("/reception/dashboard")
    public String receptionDashboard() {
        return "reception-dashboard";
    }


    @GetMapping("/lab/dashboard")
    public String labDashboard() {
        return "lab-dashboard";
    }
}