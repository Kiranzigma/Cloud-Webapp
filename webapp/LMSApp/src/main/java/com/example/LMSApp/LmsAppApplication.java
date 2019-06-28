package com.example.LMSApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class LmsAppApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(LmsAppApplication.class, args);
	}

}
