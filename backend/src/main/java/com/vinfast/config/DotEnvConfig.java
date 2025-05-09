package com.vinfast.config;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;

@Getter
public class DotEnvConfig {
    private static final Dotenv dotenv = Dotenv.configure()
            .directory("./")
            .ignoreIfMalformed()
            .ignoreIfMissing()
            .load();

    public static String getSecret() {
        return dotenv.get("SECRET");
    }

    public static String getCloudName() {
        return dotenv.get("CLOUD_NAME");
    }

    public static String getCloudApiKey() {
        return dotenv.get("CLOUD_API_KEY");
    }

    public static String getCloudApiSecret() {
        return dotenv.get("CLOUD_API_SECRET");
    }

    public static String getDbUrl() {
        return dotenv.get("DB_URL");
    }

    public static String getDbUsername() {
        return dotenv.get("DB_USERNAME");
    }

    public static String getDbPassword() {
        return dotenv.get("DB_PASSWORD");
    }
}
