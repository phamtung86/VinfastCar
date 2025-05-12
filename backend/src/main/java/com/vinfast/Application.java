package com.vinfast;

import com.vinfast.config.DotEnvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {

		System.setProperty("DB_URL", DotEnvConfig.getDbUrl());
		System.setProperty("DB_USERNAME", DotEnvConfig.getDbUsername());
		System.setProperty("DB_PASSWORD", DotEnvConfig.getDbPassword());
		System.setProperty("SECRET", DotEnvConfig.getSecret());
		Connection conn = DBConnection.getConnection();
		if (conn != null) {
			System.out.println(" Kết nối thành công!");
		} else {
			System.out.println(" Kết nối thất bại!");
		}
		SpringApplication.run(Application.class, args);
	}

}
