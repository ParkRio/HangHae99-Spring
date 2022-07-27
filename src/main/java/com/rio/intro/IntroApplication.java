package com.rio.intro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class IntroApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntroApplication.class, args);
    }

}
