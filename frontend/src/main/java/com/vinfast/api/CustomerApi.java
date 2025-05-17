package com.vinfast.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinfast.config.ApiConfig;
import com.vinfast.dto.CustomerDTO;
import com.vinfast.dto.OrderDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomerApi {
    public static List<CustomerDTO> getAllCustomers() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiConfig.BASE_URL + "/api/v1/customers"))
                .GET()
                .build();

        HttpResponse<String> response = ApiConfig.getClient().send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")); // cấu hình định dạng ngày
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // tránh lỗi khi dư field như "role"

        return mapper.readValue(response.body(), new TypeReference<List<CustomerDTO>>() {});

    }

    public static CustomerDTO getCustomerById(Long id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ApiConfig.BASE_URL + "/api/v1/customers/" + id))
                    .GET()
                    .build();

            HttpResponse<String> response = ApiConfig.getClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                return mapper.readValue(response.body(), CustomerDTO.class);
            } else {
                System.err.println("Không tìm thấy khách hàng. Mã phản hồi: " + response.statusCode());
                return null;
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Lỗi trong quá trình lấy thông tin khách hàng:");
            e.printStackTrace();
            return null;
        }
    }

    public static List<CustomerDTO> searchCustomers(String name) {
        try {
            return getAllCustomers().stream()
                    .filter(c -> c.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return List.of(); // Trả về danh sách rỗng nếu có lỗi
        }
    }

    public static void addCustomer(CustomerDTO customer) {
        try {
            // Tạo HttpRequest để gửi yêu cầu POST với dữ liệu khách hàng dưới dạng JSON
            ObjectMapper mapper = new ObjectMapper();
            String jsonBody = mapper.writeValueAsString(customer); // Chuyển CustomerDTO thành JSON

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ApiConfig.BASE_URL + "/api/v1/customers/add"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = ApiConfig.getClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Thêm khách hàng thành công!");
            } else {
                System.err.println("Lỗi khi thêm khách hàng: " + response.statusCode());
                System.err.println("Nội dung phản hồi: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Lỗi trong quá trình thêm khách hàng:");
            e.printStackTrace();
        }
    }

    public static void updateCustomer(CustomerDTO customer) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                String jsonBody = mapper.writeValueAsString(customer);

                String url = ApiConfig.BASE_URL + "/api/v1/customers/" + customer.getId();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json")
                        .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                        .build();

                HttpResponse<String> response = ApiConfig.getClient().send(request, HttpResponse.BodyHandlers.ofString());

                int statusCode = response.statusCode();
                String responseBody = response.body();

                if (statusCode == 200) {
                    System.out.println("Cập nhật khách hàng thành công!");
                } else {
                    System.err.println("Cập nhật thất bại. Mã lỗi: " + statusCode);
                    System.err.println("Phản hồi từ server: " + responseBody);
                }

            } catch (HttpTimeoutException e) {
                System.err.println("Yêu cầu cập nhật đã bị timeout.");
                e.printStackTrace();
            } catch (IOException | InterruptedException e) {
                System.err.println("Đã xảy ra lỗi khi cập nhật khách hàng:");
                e.printStackTrace();
                Thread.currentThread().interrupt(); // Khôi phục trạng thái ngắt nếu cần
            }
        }

    public static void deleteCustomer(Long customerId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ApiConfig.BASE_URL + "/api/v1/customers/" + customerId))
                    .DELETE().build();
            HttpResponse<String> response = ApiConfig.getClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("Xóa khách hàng thành công!");
            } else {
                System.err.println("Lỗi khi xóa: " + response.statusCode());
                System.err.println("Nội dung phản hồi: " + response.body());
            }
        } catch (Exception e) {
            System.err.println("Lỗi trong quá trình xóa khách hàng:");
            e.printStackTrace();
        }
    }

    public static CustomerDTO addOrderToCustomer(Long customerId, OrderDTO orderDTO) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            String jsonBody = mapper.writeValueAsString(orderDTO);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ApiConfig.BASE_URL + "/api/v1/customers/" + customerId + "/orders"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = ApiConfig.getClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Thêm Đơn hàng thành công!");
                return mapper.readValue(response.body(), CustomerDTO.class);
            } else {
                System.err.println("Lỗi khi thêm đơn hàng cho khách hàng. Mã phản hồi: " + response.statusCode());
                System.err.println("Nội dung phản hồi: " + response.body());
                return null;
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Lỗi trong quá trình thêm đơn hàng cho khách hàng:");
            e.printStackTrace();
            return null;
        }
    }

    public static boolean updateOrderStatus(Long customerId, Long orderId, String newStatus) {
        String url = String.format("%s/api/v1/customers/%d/orders/%d/status",
                ApiConfig.BASE_URL, customerId, orderId);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            String jsonBody = mapper.writeValueAsString(Map.of("status", newStatus));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = ApiConfig.getClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Trạng thái đơn hàng đã được cập nhật.");
                return true;
            }

            System.err.printf("Lỗi cập nhật (%d): %s%n", response.statusCode(), response.body());
        } catch (IOException | InterruptedException e) {
            System.err.println("Exception khi gọi API cập nhật trạng thái:");
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        return false;
    }

    public static boolean deleteOrder(Long customerId, Long orderId) {
        String url = String.format("%s/api/v1/customers/%d/orders/%d", ApiConfig.BASE_URL, customerId, orderId);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .DELETE()
                    .build();

            HttpResponse<String> response = ApiConfig.getClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 204) {
                System.out.println("Xóa đơn hàng thành công.");
                return true;
            }

            System.err.printf("Lỗi xóa đơn hàng (%d): %s%n", response.statusCode(), response.body());
        } catch (IOException | InterruptedException e) {
            System.err.println("Exception khi gọi API xóa đơn hàng:");
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        return false;
    }

}
