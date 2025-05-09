package com.vinfast.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary configKey() {

        Map<String, String> config = new HashMap<>();
        String cloudName = DotEnvConfig.getCloudName();
        String cloudApiKey = DotEnvConfig.getCloudApiKey();
        String cloudApiSecret = DotEnvConfig.getCloudApiSecret();
        config.put("cloud_name", cloudName);
        config.put("api_key", cloudApiKey);
        config.put("api_secret", cloudApiSecret);

        return new Cloudinary(config);
    }
}
