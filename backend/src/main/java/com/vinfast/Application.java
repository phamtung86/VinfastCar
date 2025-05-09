package com.vinfast;

import com.vinfast.config.DotEnvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {

		System.setProperty("DB_URL", DotEnvConfig.getDbUrl());
		System.setProperty("DB_USERNAME", DotEnvConfig.getDbUsername());
		System.setProperty("DB_PASSWORD", DotEnvConfig.getDbPassword());
		System.setProperty("SECRET", DotEnvConfig.getSecret());
		SpringApplication.run(Application.class, args);
	}

}
