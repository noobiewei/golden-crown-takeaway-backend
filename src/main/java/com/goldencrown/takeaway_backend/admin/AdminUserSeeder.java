package com.goldencrown.takeaway_backend.admin;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserSeeder implements CommandLineRunner {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserSeeder(AdminUserRepository adminUserRepository, PasswordEncoder passwordEncoder) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (adminUserRepository.count() > 0) {
            return;
        }

        String password = System.getenv("ADMIN_PASSWORD");
        if (password == null || password.isBlank()) {
            System.out.println("WARNING: ADMIN_PASSWORD is not set — skipping admin user creation. " +
                    "Set it as an environment variable and restart to create the admin login.");
            return;
        }

        adminUserRepository.save(new AdminUser("admin", passwordEncoder.encode(password)));
        System.out.println("Created admin user 'admin'.");
    }
}
