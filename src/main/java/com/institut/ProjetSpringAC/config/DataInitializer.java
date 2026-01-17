package com.institut.ProjetSpringAC.config;

import com.institut.ProjetSpringAC.entity.Role;
import com.institut.ProjetSpringAC.entity.User;
import com.institut.ProjetSpringAC.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final com.institut.ProjetSpringAC.repository.SessionRepository sessionRepository;

    @Autowired
    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder,
            com.institut.ProjetSpringAC.repository.SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create Admin
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Default Admin user created: username=admin, password=password");
        }

        // Create Academic Sessions
        if (sessionRepository.findAll().isEmpty()) {
            com.institut.ProjetSpringAC.entity.Session year = new com.institut.ProjetSpringAC.entity.Session(
                    "Année 2024/2025",
                    java.time.LocalDate.of(2024, 9, 1),
                    java.time.LocalDate.of(2025, 6, 30),
                    com.institut.ProjetSpringAC.entity.SessionType.ACADEMIC_YEAR);
            sessionRepository.save(year);

            com.institut.ProjetSpringAC.entity.Session sem1 = new com.institut.ProjetSpringAC.entity.Session(
                    "Semestre 1",
                    java.time.LocalDate.of(2024, 9, 1),
                    java.time.LocalDate.of(2025, 1, 31),
                    com.institut.ProjetSpringAC.entity.SessionType.SEMESTER);
            sem1.setParent(year);
            sessionRepository.save(sem1);

            com.institut.ProjetSpringAC.entity.Session sem2 = new com.institut.ProjetSpringAC.entity.Session(
                    "Semestre 2",
                    java.time.LocalDate.of(2025, 2, 1),
                    java.time.LocalDate.of(2025, 6, 30),
                    com.institut.ProjetSpringAC.entity.SessionType.SEMESTER);
            sem2.setParent(year);
            sessionRepository.save(sem2);

            System.out.println("Academic year 2024/2025 and semesters seeded.");
        }
    }
}
