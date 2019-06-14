package com.example.LMSApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class LmsAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(LmsAppApplication.class, args);
	}

}
