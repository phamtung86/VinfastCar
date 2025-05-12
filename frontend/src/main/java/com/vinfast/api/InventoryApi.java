package com.vinfast.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinfast.config.ApiConfig;
import com.vinfast.dto.CarDTO;
import com.vinfast.dto.InventoryDTO;
import com.vinfast.dto.InventoryTopDTO;
import com.vinfast.model.CarPageResponse;
import com.vinfast.model.InventoryPageResponse;
import com.vinfast.ui.alert.AlertNotice;
import javafx.scene.control.Alert;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class InventoryApi {
    private final AlertNotice alertNotice = new AlertNotice();

    public InventoryPageResponse getInventoryByPages(int pageNumber, int size, String sort) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiConfig.BASE_URL + "/api/v1/inventories/page?pageNumber=" + pageNumber + "&size=" + size))
                .GET()
                .build();

        HttpResponse<String> response = ApiConfig.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        // Kiểm tra mã trạng thái HTTP
        if (response.statusCode() != 200) {
            throw new IOException("Failed to fetch cars: " + response.statusCode() + " " + response.body());
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.body(), InventoryPageResponse.class);
    }

    public List<InventoryDTO> getAllInventories() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ApiConfig.BASE_URL + "/api/v1/inventories"))
                    .GET()
                    .build();

            HttpResponse<String> response = ApiConfig.getClient().send(request, HttpResponse.BodyHandlers.ofString());

            // Kiểm tra mã trạng thái HTTP
            if (response.statusCode() != 200) {
                throw new IOException("Failed to fetch cars: " + response.statusCode() + " " + response.body());
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), new TypeReference<List<InventoryDTO>>() {
            });
        }catch (Exception e){
            e.printStackTrace();
        }
       return null;
    }

    public List<InventoryTopDTO> getInventoryTop() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ApiConfig.BASE_URL + "/api/v1/inventories/top"))
                    .GET()
                    .build();

            HttpResponse<String> response = ApiConfig.getClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
//                alertNotice.showAlert("Lỗi", "Không thể tải dữ liệu kho top!", Alert.AlertType.ERROR);
                return List.of();
            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), new TypeReference<List<InventoryTopDTO>>() {});
        } catch (Exception e) {
            e.printStackTrace();
//            alertNotice.showAlert("Lỗi", "Đã xảy ra lỗi khi tải kho top!", Alert.AlertType.ERROR);
        }
        return List.of();
    }

    public int addInventory(InventoryDTO inventory) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(inventory);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ApiConfig.BASE_URL + "/api/v1/inventories"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int updateInventory(InventoryDTO inventoryDTO) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(inventoryDTO);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ApiConfig.BASE_URL + "/api/v1/inventories"))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int deleteAInventory(long inventoryID) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ApiConfig.BASE_URL + "/api/v1/inventories/" + inventoryID))
                    .DELETE()
                    .build();

            HttpResponse<String> response = ApiConfig.getClient().send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

}
