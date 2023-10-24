package com.vetrikos.inventory.system;

import com.vetrikos.inventory.system.classes.User;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class Application {
    private final UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }

    @Bean
    public CommandLineRunner testFunction() {
        return args -> {
            userRepository.save(User.builder().username("meno").isManager(false).build());
            System.out.println(userRepository.findAll());
        };
    }

}
