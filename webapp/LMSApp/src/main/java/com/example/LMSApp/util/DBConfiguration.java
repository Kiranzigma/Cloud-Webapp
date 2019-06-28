package com.example.LMSApp.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties("spring.datasource")
@SuppressWarnings("unused")
public class DBConfiguration {

	@Value("${spring.datasource.url}")
	private String url;
	
	@Value("${environment}")
	private String environment;
	
	@Profile("local")
	@Bean
	public String localDatabaseConnection() {
		System.out.println(environment + " DB Connection");
		System.out.println(url);
		return environment + "DB Connection";
		
	}
	
	@Profile("cloud")
	@Bean
	public String cloudDatabaseConnection() {
		System.out.println(environment + " DB Connection");
		System.out.println(url);
		return environment + "DB Connection";
	}
}
