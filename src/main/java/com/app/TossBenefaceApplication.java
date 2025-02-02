package com.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication(scanBasePackages = "com.app")
@EnableConfigurationProperties
public class TossBenefaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TossBenefaceApplication.class, args);
	}

}
