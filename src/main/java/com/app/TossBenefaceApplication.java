package com.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.app")
public class TossBenefaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TossBenefaceApplication.class, args);
	}

}
