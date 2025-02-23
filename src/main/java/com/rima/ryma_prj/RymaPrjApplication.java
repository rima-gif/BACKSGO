package com.rima.ryma_prj;

import com.rima.ryma_prj.domain.model.Role;
import com.rima.ryma_prj.domain.model.User;
import com.rima.ryma_prj.domain.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@SpringBootApplication
public class RymaPrjApplication {

    public static void main(String[] args) {
        SpringApplication.run(RymaPrjApplication.class, args);
    }

    @Bean
    CommandLineRunner initSuperAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("superadmin@example.com").isEmpty()) {
                User superAdmin = new User();
                superAdmin.setEmail("superadmin@example.com");
                superAdmin.setPassword(passwordEncoder.encode("SuperSecurePassword"));

                superAdmin.setUsername("superadmin");
                superAdmin.setRoles(Collections.singleton(Role.ROLE_SUPER_ADMIN));

                // Correct way to assign the role:

                userRepository.save(superAdmin);



                System.out.println("✅ Super Admin created successfully!");
            } else {
                System.out.println("⚠️ Super Admin already exists.");
            }
        };
    }

}
