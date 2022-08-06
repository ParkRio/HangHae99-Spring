package com.example.jwtproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class JwtprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtprojectApplication.class, args);
    }

}
