package com.vinfast.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinfast.config.ApiConfig;
import com.vinfast.dto.CarDTO;
import com.vinfast.dto.OrderDTO;
import com.vinfast.model.CarPageResponse;
import com.vinfast.model.OrderPageResponse;
import com.vinfast.ui.alert.AlertNotice;
import javafx.scene.control.Alert;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class OrderApi {
    private final AlertNotice alertNotice = new AlertNotice();
    public static List<OrderDTO> getAllOrder() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiConfig.BASE_URL + "/api/v1/orders"))
                .GET()
                .build();

        HttpResponse<String> response = ApiConfig.getClient().send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.body(), new TypeReference<>() {
        });
    }
    public OrderPageResponse getAllCarsByPages(int pageNumber, int size, String sort) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiConfig.BASE_URL + "/api/v1/orders/page?pageNumber=" + pageNumber + "&size=" + size + "&sort=" + sort))
                .GET()
                .build();

        HttpResponse<String> response = ApiConfig.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        // Kiểm tra mã trạng thái HTTP
        if (response.statusCode() != 200) {
            throw new IOException("Failed to fetch cars: " + response.statusCode() + " " + response.body());
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.body(), OrderPageResponse.class);
    }
    public void editOrder(OrderDTO order) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(order);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ApiConfig.BASE_URL + "/api/v1/orders"))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                alertNotice.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật thành công", null);
            } else {
                // Nếu mã lỗi khác 200, hiển thị thông báo lỗi cho người dùng
                String errorMessage = "Cập nhật thất bại. Lỗi: " + response.statusCode() + " - " + response.body();
                alertNotice.showAlert(Alert.AlertType.ERROR, "Thất bại", errorMessage, null);
                System.out.println("Error Response: " + response.body());
            }
        } catch (Exception e) {
            // Xử lý trường hợp ngoại lệ và thông báo lỗi chi tiết
            e.printStackTrace();
            alertNotice.showAlert(Alert.AlertType.ERROR, "Lỗi", "Đã xảy ra lỗi khi cập nhật đơn hàng: " + e.getMessage(), null);
        }
    }


    public int deleteOrder(Long orderId) {
        int statusCode = 0;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ApiConfig.BASE_URL + "/api/v1/orders/" + orderId))
                    .DELETE()
                    .build();

            HttpResponse<String> response = ApiConfig.getClient().send(request, HttpResponse.BodyHandlers.ofString());

            // Kiểm tra mã trạng thái HTTP
            if (response.statusCode() != 200) {
                throw new IOException("Failed to fetch cars: " + response.statusCode() + " " + response.body());
            }
            statusCode = response.statusCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusCode;
    }
    public void addOrder(OrderDTO order) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(order);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ApiConfig.BASE_URL + "/api/v1/orders"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            // Kiểm tra mã phản hồi
            int responseCode = response.statusCode();
            if (responseCode == 200 || responseCode == 201) {
                System.out.println("Thêm xe thành công.");
            } else {
                System.err.println("Lỗi khi thêm xe: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
