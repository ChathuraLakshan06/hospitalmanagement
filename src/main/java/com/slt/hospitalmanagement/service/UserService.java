package com.slt.hospitalmanagement.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.slt.hospitalmanagement.entity.User;
import com.slt.hospitalmanagement.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getByUsername(String username) {

        return userRepository
                .findByUsername(username)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );
    }

    public void changePassword(
            String username,
            String currentPassword,
            String newPassword,
            String confirmPassword) {

        User user = getByUsername(username);

        if (!passwordEncoder.matches(
                currentPassword,
                user.getPassword())) {

            throw new IllegalArgumentException(
                    "Current password is incorrect."
            );
        }

        if (newPassword == null ||
                newPassword.length() < 8) {

            throw new IllegalArgumentException(
                    "New password must contain at least 8 characters."
            );
        }

        if (!newPassword.equals(confirmPassword)) {

            throw new IllegalArgumentException(
                    "New password and confirmation do not match."
            );
        }

        if (passwordEncoder.matches(
                newPassword,
                user.getPassword())) {

            throw new IllegalArgumentException(
                    "New password must be different from current password."
            );
        }

        user.setPassword(
                passwordEncoder.encode(newPassword)
        );

        userRepository.save(user);
    }
}