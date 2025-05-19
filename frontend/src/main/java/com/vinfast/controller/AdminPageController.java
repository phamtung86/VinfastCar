package com.vinfast.controller;

import com.vinfast.api.CarApi;
import com.vinfast.api.InventoryApi;
import com.vinfast.dto.CarDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinfast.dto.OrderChartDTO;
import com.vinfast.ui.chart.CarBarChart;
import com.vinfast.dto.InventoryDTO;
import com.vinfast.dto.InventoryTopDTO;
import com.vinfast.ui.chart.InventoryChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
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
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class AdminPageController implements Initializable {
    @FXML
    private HBox contentBox;
    @FXML
    private VBox mainContainer;
    private Node savedContentBox; // Lưu contentBox gốc để khôi phục khi cần
    @FXML
    private LineChart<String, Number> orderFlowchart;// Lưu contentBox gốc để khôi phục khi cần
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
//    @FXML
//    private ChoiceBox<String> logChoice;

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


    ObservableList<String> list = FXCollections.observableArrayList("LogOut");
    private CarApi carApi = new CarApi();
    public List<CarDTO> cars = carApi.getAllCars();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initInventoryChart();
        showCarBarChart();
        showCountAllCars();
    }

    public void showCountAllCars() {
        countAllCars.setText(String.valueOf(cars.size()));
    }

    public void showCarBarChart (){
        CarBarChart barChart = new CarBarChart(cars, "Phân phối xe theo trạng thái");
        pieChartContainer.getChildren().add(barChart); // Line 73
        if (salesLineChartContainer == null) {
            System.err.println("salesLineChartContainer is null!");
            return;
        }
        initOrderFlowChart();
    }

    @FXML
    private void handleChoiceBoxSelection(ActionEvent e) {
//        String selectedBox = logChoice.getValue();
//        if ("LogOut".equals(selectedBox)) {
//            loadLoginPage();
//        }
    }

    @FXML
    public void initOrderFlowChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Số đơn hàng theo ngày");

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

    private record Warehouse(String name, int capacity, int carCount) {}


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
    @FXML
    private void onMouseEntered(MouseEvent event) {
        Object source = event.getSource();
        if (source instanceof HBox) {
            HBox hbox = (HBox) source;
            // đổi màu nền khi hover
            hbox.setStyle("-fx-background-color: #ADD8E6;"); // màu xanh nhạt ví dụ
//            hbox.setPadding(Insets.EMPTY(= new (10)));
        }
    }

    @FXML
    private void onMouseExited(MouseEvent event) {
        Object source = event.getSource();
        if (source instanceof HBox) {
            HBox hbox = (HBox) source;
            // reset màu nền về mặc định (hoặc style css cũ)
            hbox.setStyle("");
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