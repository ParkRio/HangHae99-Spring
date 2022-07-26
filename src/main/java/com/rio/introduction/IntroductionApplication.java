package com.rio.introduction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // Todo : Auditing는 자동설정 되지 않으므로 아래와 같이 수동으로 설정
@SpringBootApplication
public class IntroductionApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntroductionApplication.class, args);
	}

}
