package com.slt.hospitalmanagement.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.slt.hospitalmanagement.entity.Role;
import com.slt.hospitalmanagement.entity.User;
import com.slt.hospitalmanagement.repository.RoleRepository;
import com.slt.hospitalmanagement.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        Role adminRole = createRole("ADMIN");
        Role doctorRole = createRole("DOCTOR");
        Role receptionistRole = createRole("RECEPTIONIST");
        Role labStaffRole = createRole("LAB_STAFF");

        createRole("NURSE");
        createRole("LAB_STAFF");
        createRole("PHARMACIST");
        createRole("ACCOUNTANT");

        createUser(
                "admin",
                "Admin@123",
                adminRole
        );

        createUser(
                "doctor",
                "Doctor@123",
                doctorRole
        );

        createUser(
                "receptionist",
                "Reception@123",
                receptionistRole
        );
        createUser(
        "labstaff",
        "Lab@123",
        labStaffRole
);
    }

    private Role createRole(String roleName) {

        return roleRepository.findByName(roleName)
                .orElseGet(() ->
                        roleRepository.save(new Role(roleName)));
    }

    private void createUser(
            String username,
            String rawPassword,
            Role role) {

        if (userRepository.findByUsername(username).isEmpty()) {

            User user = new User();

            user.setUsername(username);

            user.setPassword(
                    passwordEncoder.encode(rawPassword)
            );

            user.setEnabled(true);
            user.setRole(role);

            userRepository.save(user);
        }
    }
}