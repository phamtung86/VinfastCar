package com.vinfast.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinfast.api.CarApi;
import com.vinfast.api.CustomerApi;
import com.vinfast.api.InventoryApi;
import com.vinfast.api.OrderApi;
import com.vinfast.dto.*;
import com.vinfast.ui.chart.CarBarChart;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class AdminPageController implements Initializable {
    private final OrderApi orderApi = new OrderApi();

    @FXML
    private HBox contentBox;

    @FXML
    private VBox mainContainer;

    private Node savedContentBox; // Lưu contentBox gốc để khôi phục khi cần

    @FXML
    private LineChart<String, Number> orderFlowchart;// Lưu contentBox gốc để khôi phục khi cần

    @FXML
    private LineChart<String, Number> orderToMonthlyFlowchart;

    @FXML
    private StackedBarChart<String, Number> customerWithOrderToMonthlyFlowchart;

    @FXML
    private Label revenueID;
    @FXML
    private StackedBarChart<String, Number> statusOrder;

    @FXML
    private CategoryAxis xAxisOrder;

    @FXML
    private NumberAxis yAxisOrder;


    private void loadPage(String fxmlFile) {
        try {
            if (savedContentBox == null) {
                savedContentBox = contentBox; // Cập nhật contentBox mỗi lần thay đổi
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            HBox newContent = loader.load();
            newContent.setMaxHeight(Double.MAX_VALUE);
            newContent.setMaxWidth(Double.MAX_VALUE);
            VBox.setVgrow(newContent, Priority.ALWAYS);

            int index = mainContainer.getChildren().indexOf(contentBox);
            if (index != -1) {
                mainContainer.getChildren().set(index, newContent);
                contentBox = newContent; // Cập nhật contentBox hiện tại
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private HBox chartContainer;

    @FXML
    private VBox salesLineChartContainer;

    @FXML
    private StackedBarChart<String, Number> inventoryChart;

    @FXML
    private CategoryAxis inventoryChartXAxis;

    @FXML
    private NumberAxis inventoryChartYAxis;

    @FXML
    private VBox pieChartContainer;

    @FXML
    private Label countAllCars;

    @FXML
    private HBox dashBoard;

    @FXML
    private HBox manageCar;

    @FXML
    private HBox manageClient;

    @FXML
    private HBox manageWarehouse;

    @FXML
    private HBox support;

    @FXML
    private HBox report;

    @FXML
    private Label totalInventories;

    @FXML
    private Label countAllCustomers;

    ObservableList<String> list = FXCollections.observableArrayList("LogOut");
    private final CarApi carApi = new CarApi();
    private final InventoryApi inventoryApi = new InventoryApi();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initInventoryChart();
        List<CarDTO> cars = carApi.getAllCars();
        List<InventoryDTO> inventoryDTOS = inventoryApi.getAllInventories();
        showCarBarChart(cars);
        showCountAllCars(cars);
        showTotalInventories(inventoryDTOS);
        showCountAllCustomers();
        orderFluctuationToMonthly();
        monthlyCustomerOrderTrends();
        initOrderFlowChart();
        loadRevenue();
        loadOrderStatusChart();
    }

    private void loadRevenue() {
        new Thread(() -> {
            long revenue = orderApi.getRevenue();
            String formatted = String.format("%,d VND", revenue); // Ví dụ: 123,456,789 VND
            Platform.runLater(() -> revenueID.setText(formatted));
        }).start();
    }

    public void showCountAllCars(List<CarDTO> cars) {
        countAllCars.setText(String.valueOf(cars.size()));
    }

    public void showTotalInventories(List<InventoryDTO> inventoryDTOS) {
        totalInventories.setText(String.valueOf(inventoryDTOS.size()));
    }

    public void showCountAllCustomers() {
        try {
            List<CustomerDTO> customers = CustomerApi.getAllCustomers();
            int totalCustomers = customers.size();
            countAllCustomers.setText(String.valueOf(totalCustomers));
        } catch (IOException | InterruptedException e) {
            System.err.println("Lỗi khi lấy tổng số khách hàng: " + e.getMessage());
            countAllCustomers.setText("N/A"); // Hiển thị giá trị mặc định nếu lỗi
        }
    }

    public void showCarBarChart(List<CarDTO> cars) {
        CarBarChart barChart = new CarBarChart(cars, "Phân phối xe theo trạng thái");
        pieChartContainer.getChildren().add(barChart);
        if (salesLineChartContainer == null) {
            System.err.println("salesLineChartContainer is null!");
            return;
        }
    }

    public void orderFluctuationToMonthly() { //Biến động đơn hàng theo tháng
        try {
            // Lấy dữ liệu đơn hàng theo tháng
            Map<String, Long> ordersByMonth = getOrdersByMonth();

            // Cấu hình và hiển thị biểu đồ
            monthlyOrderFlowChart(ordersByMonth);
        } catch (IOException | InterruptedException e) {
            System.err.println("Lỗi khi lấy dữ liệu đơn hàng: " + e.getMessage());
        }
    }

    public void monthlyCustomerOrderTrends() {
        try {
            // Lấy dữ liệu khách hàng đặt hàng theo tháng
            Map<String, Long> customersByMonth = getCustomersWithOrdersByMonth();
            // Cấu hình và hiển thị biểu đồ
            monthlyCustomerFlowChart(customersByMonth);
        } catch (IOException | InterruptedException e) {
            System.err.println("Lỗi khi lấy dữ liệu khách hàng đặt hàng: " + e.getMessage());
        }
    }

    private Map<String, Long> getOrdersByMonth() throws IOException, InterruptedException {
        // Lấy danh sách khách hàng
        List<CustomerDTO> customers = CustomerApi.getAllCustomers();

        // Thu thập tất cả đơn hàng và nhóm theo tháng
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM-yyyy");
        return customers.stream()
                .flatMap(customer -> customer.getOrders().stream()) // Lấy tất cả đơn hàng
                .collect(Collectors.groupingBy(
                        order -> monthFormat.format(order.getOrderDate()), // Nhóm theo năm-tháng
                        Collectors.counting() // Đếm số đơn hàng
                ));
    }

    private void monthlyOrderFlowChart(Map<String, Long> ordersByMonth) {
        // Tạo dữ liệu cho biểu đồ
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Số lượng đơn hàng");

        // Sắp xếp các tháng và thêm dữ liệu vào series
        ordersByMonth.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // Sắp xếp theo tháng
                .forEach(entry -> series.getData().add(
                        new XYChart.Data<>(entry.getKey(), entry.getValue())
                ));

        // Cấu hình trục của biểu đồ
        orderToMonthlyFlowchart.setTitle("Biến động số lượng đơn hàng theo tháng");
        CategoryAxis xAxis = (CategoryAxis) orderToMonthlyFlowchart.getXAxis();
        xAxis.setLabel("Tháng");
        NumberAxis yAxis = (NumberAxis) orderToMonthlyFlowchart.getYAxis();
        yAxis.setLabel("Số lượng đơn hàng");

        // Xóa dữ liệu cũ và thêm dữ liệu mới
        orderToMonthlyFlowchart.getData().clear();
        orderToMonthlyFlowchart.getData().add(series);
    }

    private Map<String, Long> getCustomersWithOrdersByMonth() throws IOException, InterruptedException {
        // Lấy danh sách khách hàng
        List<CustomerDTO> customers = CustomerApi.getAllCustomers();

        // Thu thập tất cả đơn hàng và nhóm theo tháng, đếm số lượng khách hàng duy nhất
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM-yyyy");
        Map<String, Set<Long>> customersByMonth = customers.stream()
                .filter(customer -> !customer.getOrders().isEmpty()) // Chỉ lấy khách hàng có đơn hàng
                .flatMap(customer -> customer.getOrders().stream()
                        .map(order -> new Object() {
                            String month = monthFormat.format(order.getOrderDate());
                            Long customerId = customer.getId();
                        }))
                .collect(Collectors.groupingBy(
                        obj -> obj.month,
                        Collectors.mapping(obj -> obj.customerId, Collectors.toSet())
                ));

        // Chuyển Set<Long> thành số lượng khách hàng
        return customersByMonth.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> (long) entry.getValue().size()
                ));
    }

    private void monthlyCustomerFlowChart(Map<String, Long> customersByMonth) {
        // Tạo dữ liệu cho biểu đồ
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Số lượng khách hàng đặt hàng");

        // Sắp xếp các tháng và thêm dữ liệu vào series
        customersByMonth.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // Sắp xếp theo tháng
                .forEach(entry -> series.getData().add(
                        new XYChart.Data<>(entry.getKey(), entry.getValue())
                ));

        // Cấu hình trục của biểu đồ
        customerWithOrderToMonthlyFlowchart.setTitle("Biến động số lượng khách hàng đặt hàng theo tháng");
        CategoryAxis xAxis = (CategoryAxis) customerWithOrderToMonthlyFlowchart.getXAxis();
        xAxis.setLabel("Tháng");
        NumberAxis yAxis = (NumberAxis) customerWithOrderToMonthlyFlowchart.getYAxis();
        yAxis.setLabel("Số lượng khách hàng");

        // Xóa dữ liệu cũ và thêm dữ liệu mới
        customerWithOrderToMonthlyFlowchart.getData().clear();
        customerWithOrderToMonthlyFlowchart.getData().add(series);
    }

    @FXML
    public void initOrderFlowChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Số đơn hàng theo ngày trong tháng");

        try {
            // Gọi API
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/v1/orders/chart-data"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            // Parse JSON
            ObjectMapper mapper = new ObjectMapper();
            List<OrderChartDTO> data = Arrays.asList(
                    mapper.readValue(response.body(), OrderChartDTO[].class)
            );

            // Thêm dữ liệu vào chart
            for (OrderChartDTO dto : data) {
                series.getData().add(new XYChart.Data<>(dto.getDate(), dto.getCount()));
            }

            orderFlowchart.getData().add(series);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void loadOrderStatusChart() {
        // URL API của bạn
        String apiUrl = "http://localhost:8080/api/v1/orders/status-count";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        List<OrderChartDTO> data = mapper.readValue(json, new TypeReference<>() {});

                        // JavaFX UI updates must run on JavaFX thread
                        Platform.runLater(() -> {
                            statusOrder.getData().clear();
                            xAxisOrder.setLabel("Trạng thái đơn hàng");
                            yAxisOrder.setLabel("Số lượng");

                            XYChart.Series<String, Number> series = new XYChart.Series<>();
                            series.setName("Trạng thái");

                            for (OrderChartDTO dto : data) {
                                series.getData().add(new XYChart.Data<>(dto.getDate(), dto.getCount()));
                            }

                            statusOrder.getData().add(series);
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
    }

    private void loadLoginPage() {
        try {
            // Load trang Login
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/vinfast/fe/Login.fxml"));
            Parent root = fxmlLoader.load();

            // Tạo Stage mới
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.setTitle("Login - VinFast");
            loginStage.initStyle(StageStyle.UNDECORATED); // Đặt không có viền cửa sổ

            // Lấy Stage hiện tại và đóng nó
//            Stage currentStage = (Stage) logChoice.getScene().getWindow();
//            currentStage.close();

            // Hiển thị cửa sổ login mới
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể tải lại trang đăng nhập!");
        }
    }

    public void initInventoryChart() {
        // Gán nhãn cho trục
        inventoryChartXAxis.setLabel("Tên kho");
        inventoryChartYAxis.setLabel("Số xe");
        inventoryChartYAxis.setTickLabelFill(Color.LIGHTSKYBLUE);

        // Cấu hình biểu đồ
        inventoryChart.setTitle("Số xe theo 5 kho chứa nhiều nhất");
        inventoryChart.setLegendVisible(false);
        inventoryChart.setAnimated(false);

        // Xóa dữ liệu cũ
        inventoryChart.getData().clear();

        // Tạo series dữ liệu
        XYChart.Series<String, Number> carCountSeries = new XYChart.Series<>();

        try {
            // Gọi API để lấy dữ liệu thật
            InventoryApi inventoryApi = new InventoryApi();
            List<InventoryTopDTO> topInventories = inventoryApi.getInventoryTop();

            // Chuyển dữ liệu từ DTO sang series
            for (InventoryTopDTO dto : topInventories) {
                XYChart.Data<String, Number> data = new XYChart.Data<>(dto.getName(), dto.getCarCount());
                carCountSeries.getData().add(data);
            }

            // Thêm series vào biểu đồ
            inventoryChart.getData().add(carCountSeries);
        } catch (Exception e) {
            e.printStackTrace();
            // Gợi ý: Hiển thị thông báo nếu API lỗi
            // alertNotice.showError("Lỗi khi tải dữ liệu kho", e.getMessage());
        }
    }

    private record Warehouse(String name, int capacity, int carCount) {
    }

    private void showAlert(String title, String message) {
        System.out.println(title + ": " + message);
    }

    public void moveToDashBoard(MouseEvent mouseEvent) {
        if (savedContentBox != null) {
            int index = mainContainer.getChildren().indexOf(contentBox);
            if (index != -1) {
                mainContainer.getChildren().set(index, savedContentBox); // Phục hồi contentBox gốc
                contentBox = (HBox) savedContentBox; // Cập nhật lại tham chiếu
            }
        }
    }

    public void moveToManageCar(MouseEvent mouseEvent) {
        loadPage("/com/vinfast/fe/ManageCar.fxml");
    }

    public void moveToManageClient(MouseEvent mouseEvent) {
        loadPage("/com/vinfast/fe/ManageClient.fxml");
    }

    public void moveToWarehouse(MouseEvent mouseEvent) {
        loadPage("/com/vinfast/fe/Warehouse.fxml");
    }

    public void moveToSupport(MouseEvent mouseEvent) {
        loadPage("/com/vinfast/fe/Support.fxml");
    }

    public void moveToReport(MouseEvent mouseEvent) {
        loadPage("/com/vinfast/fe/Report.fxml");
    }
}