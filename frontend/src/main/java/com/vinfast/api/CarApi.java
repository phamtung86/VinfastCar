package com.vinfast.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinfast.config.ApiConfig;
import com.vinfast.dto.CarDTO;
import com.vinfast.model.CarPageResponse;
import com.vinfast.ui.alert.AlertNotice;

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
import java.util.Map;

public class CarApi {

    private final AlertNotice alertNotice = new AlertNotice();

    public CarPageResponse getAllCarsByPages(int pageNumber, int size, String sort) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiConfig.BASE_URL + "/api/v1/cars/page?pageNumber=" + pageNumber + "&size=" + size + "&sort=" + sort))
                .GET()
                .build();

        HttpResponse<String> response = ApiConfig.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        // Kiểm tra mã trạng thái HTTP
        if (response.statusCode() != 200) {
            throw new IOException("Failed to fetch cars: " + response.statusCode() + " " + response.body());
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.body(), CarPageResponse.class);
    }

    public List<CarDTO> getAllCars() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ApiConfig.BASE_URL + "/api/v1/cars"))
                    .GET()
                    .build();

            HttpResponse<String> response = ApiConfig.getClient().send(request, HttpResponse.BodyHandlers.ofString());

            // Kiểm tra mã trạng thái HTTP
            if (response.statusCode() != 200) {
                throw new IOException("Failed to fetch cars: " + response.statusCode() + " " + response.body());
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), new TypeReference<List<CarDTO>>() {

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<CarDTO> getCarByStatus(String status)  {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ApiConfig.BASE_URL + "/api/v1/cars/status?status=" + status ))

                    .GET()
                    .build();

            HttpResponse<String> response = ApiConfig.getClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new IOException("Failed to fetch cars: " + response.statusCode() + " " + response.body());
            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), new TypeReference<List<CarDTO>>() {
            });

        } catch (Exception e){

            e.printStackTrace();
        }
        return null;
    }

//    public int countAllCars() {
//        try {
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create(ApiConfig.BASE_URL + "/api/v1/cars/count"))
//                    .GET()
//                    .build();
//
//            HttpResponse<String> response = ApiConfig.getClient().send(request, HttpResponse.BodyHandlers.ofString());
//
//            if (response.statusCode() != 200) {
//                throw new IOException("Failed to fetch cars: " + response.statusCode() + " " + response.body());
//            }
//
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.readValue(response.body(), new TypeReference<Integer>() {
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
//

    public void addCar(CarDTO car, List<File> imageFiles) {
        String boundary = Long.toHexString(System.currentTimeMillis()); // Tạo một boundary ngẫu nhiên
        String CRLF = "\r\n"; // Ký tự xuống dòng

        try {
            URL url = new URL(ApiConfig.BASE_URL + "/api/v1/cars");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            conn.setDoOutput(true);

            // Tạo OutputStream để gửi dữ liệu
            try (OutputStream os = conn.getOutputStream();
                 PrintWriter writer = new PrintWriter(new OutputStreamWriter(os, "UTF-8"))) {

                // Gửi dữ liệu JSON của CarDTO
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(car);
                writer.append("--" + boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"car\"").append(CRLF);
                writer.append("Content-Type: application/json; charset=UTF-8").append(CRLF);
                writer.append(CRLF).append(json).append(CRLF).flush();

                // Gửi từng tệp hình ảnh
                for (File imageFile : imageFiles) {
                    writer.append("--" + boundary).append(CRLF);
                    writer.append("Content-Disposition: form-data; name=\"images\"; filename=\"" + imageFile.getName() + "\"").append(CRLF);
                    writer.append("Content-Type: " + HttpURLConnection.guessContentTypeFromName(imageFile.getName())).append(CRLF);
                    writer.append(CRLF).flush();

                    // Ghi tệp vào OutputStream
                    try (FileInputStream inputStream = new FileInputStream(imageFile)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                    }
                    os.flush();
                    writer.append(CRLF).flush(); // Kết thúc phần tệp
                }

                // Kết thúc yêu cầu
                writer.append("--" + boundary + "--").append(CRLF).flush();
            }

            // Kiểm tra mã phản hồi
            int responseCode = conn.getResponseCode();
            if (responseCode == 200 || responseCode == 201) {
                System.out.println("Thêm xe thành công.");
            } else {
                System.err.println("Lỗi khi thêm xe: " + responseCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int editCar(CarDTO car) {
        int status = 0;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(car);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ApiConfig.BASE_URL + "/api/v1/cars"))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            status = response.statusCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public int deleteCar(int carId) {
        int statusCode = 0;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ApiConfig.BASE_URL + "/api/v1/cars/" + carId))
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

    public List<CarDTO> searchCars(String name) throws IOException, InterruptedException {
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
        String url = ApiConfig.BASE_URL + "/api/v1/cars/search?name=" + encodedName;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = ApiConfig.getClient().send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.body(), new TypeReference<>() {
        });
    }

    public CarDTO findCarById(Integer id) throws IOException, InterruptedException {
        String url = ApiConfig.BASE_URL + "/api/v1/cars/id/" + id;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = ApiConfig.getClient().send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.body(), new TypeReference<>() {
        });
    }

    public static boolean updateCarStatus(Long carId, String newStatus) {
        // Đổi URL cho đúng với controller bạn đã viết: /update-status/{id}
        String url = String.format("%s/api/v1/cars/update-status/%d", ApiConfig.BASE_URL, carId);

        ObjectMapper mapper = new ObjectMapper();

        try {
            // Gửi JSON với key "status" khớp controller
            String jsonBody = mapper.writeValueAsString(Map.of("status", newStatus));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = ApiConfig.getClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Cập nhật trạng thái xe thành công.");
                return true;
            } else {
                System.err.printf("Lỗi cập nhật trạng thái xe (%d): %s%n", response.statusCode(), response.body());
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Lỗi khi gọi API cập nhật trạng thái xe:");
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        return false;
    }
}
