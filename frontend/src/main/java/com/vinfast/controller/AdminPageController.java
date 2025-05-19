package com.vinfast.controller;

import com.vinfast.api.CarApi;
import com.vinfast.dto.CarDTO;
import com.vinfast.ui.chart.CarPieChart;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinfast.dto.OrderChartDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
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
    @FXML
    private ChoiceBox<String> logChoice;

    @FXML
    private VBox salesLineChartContainer;
    @FXML
    private VBox pieChartContainer;
    ObservableList<String> list = FXCollections.observableArrayList("LogOut");
    private CarApi carApi;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carApi = new CarApi();
        logChoice.setItems(list);
        List<CarDTO> cars = carApi.getAllCars();
        CarPieChart pieChart = new CarPieChart(cars, "Phân phối xe theo trạng thái");
        pieChartContainer.getChildren().add(pieChart); // Line 73
        if (salesLineChartContainer == null) {
            System.err.println("salesLineChartContainer is null!");
            return;
        }
        initOrderFlowChart();
    }

    @FXML
    private void handleChoiceBoxSelection(ActionEvent e) {
        String selectedBox = logChoice.getValue();
        if ("LogOut".equals(selectedBox)) {
            loadLoginPage();
        }
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
            Stage currentStage = (Stage) logChoice.getScene().getWindow();
            currentStage.close();

            // Hiển thị cửa sổ login mới
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể tải lại trang đăng nhập!");
        }
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