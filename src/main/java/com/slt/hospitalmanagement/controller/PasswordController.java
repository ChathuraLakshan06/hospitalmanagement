package com.slt.hospitalmanagement.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.slt.hospitalmanagement.service.UserService;

@Controller
public class PasswordController {

    private final UserService userService;

    public PasswordController(
            UserService userService) {

        this.userService = userService;
    }

    @GetMapping("/change-password")
    public String changePasswordPage() {

        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(

            @RequestParam String currentPassword,

            @RequestParam String newPassword,

            @RequestParam String confirmPassword,

            Authentication authentication,

            Model model) {

        try {

            userService.changePassword(
                    authentication.getName(),
                    currentPassword,
                    newPassword,
                    confirmPassword
            );

            model.addAttribute(
                    "success",
                    "Password changed successfully."
            );

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "change-password";
    }
}