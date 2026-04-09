package com.student.scheduling;

import com.student.scheduling.entity.User;
import com.student.scheduling.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner normalizeLegacyInstructorUsers(UserRepository userRepository) {
        return args -> {
            for (User user : userRepository.findByRole("instructor")) {
                user.setRole("admin");
                userRepository.save(user);
            }
        };
    }
}
