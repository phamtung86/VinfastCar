package com.vinfast.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinfast.config.ApiConfig;
import com.vinfast.dto.CarDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class CarApi {

    public static List<CarDTO> getCarsFromApi() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiConfig.BASE_URL + "/api/v1/cars"))
                .GET()
                .build();

        HttpResponse<String> response = ApiConfig.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.body(), new TypeReference<>() {});
    }
}
