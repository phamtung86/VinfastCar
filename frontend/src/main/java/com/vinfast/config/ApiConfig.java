package com.vinfast.config;

import java.net.http.HttpClient;

public class ApiConfig {
    public static final String BASE_URL = "http://localhost:8080";

    private static final HttpClient client = HttpClient.newHttpClient();

    public static HttpClient getClient() {
        return client;
    }
}
