package com.santander.techdrops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.santander.techdrops")

public class TechdropsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechdropsApplication.class, args);
	}

}
