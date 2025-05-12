package com.vinfast.controller;

import com.vinfast.api.CarApi;
import com.vinfast.dto.CarDTO;
import com.vinfast.dto.LibraryDTO;
import com.vinfast.model.CarPageResponse;
import com.vinfast.ui.car.CarActionHandler;
import com.vinfast.ui.chart.CarScatterChart;
import com.vinfast.ui.table.CarTableConfigurer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ManageCarController implements Initializable {

    @FXML
    private TableView<CarDTO> carTable;

    @FXML
    private TableColumn<CarDTO, Integer> idCol;

    @FXML
    private TableColumn<CarDTO, String> nameCol;

    @FXML
    private TableColumn<CarDTO, List<LibraryDTO>> imageCol;

    @FXML
    private TableColumn<CarDTO, Double> odoCol;

    @FXML
    private TableColumn<CarDTO, Integer> yearCol;

    @FXML
    private TableColumn<CarDTO, String> gearCol;

    @FXML
    private TableColumn<CarDTO, String> originalCol;

    @FXML
    private TableColumn<CarDTO, Long> priceCol;

    @FXML
    private TableColumn<CarDTO, String> statusCol;

    @FXML
    private TableColumn<CarDTO, String> engineCol;

    @FXML
    private Pane boxFeatureSearch;

    @FXML
    private TextField searchField;

    @FXML
    private Button btnPrev;

    @FXML
    private Label lblPage;

    @FXML
    private Button btnNext;


    private int currentPage = 1;
    private final int pageSize = 10;
    private long totalCars = 0;
    private boolean isLastPage = false;
    private boolean isFirstPage = true;
    private CarActionHandler actionHandler;
    private CarApi carApi;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        actionHandler = new CarActionHandler(carTable, this::updateTableData);
        carApi = new CarApi();

        CarTableConfigurer tableConfigurer = new CarTableConfigurer(
                carTable, idCol, nameCol, imageCol, odoCol, yearCol,
                gearCol, originalCol, priceCol, statusCol, engineCol
        );
        tableConfigurer.configureSize();
        tableConfigurer.configureStyle();
        tableConfigurer.setDataIntoTableView();
        loadDataFromApi();
    }

    private void loadDataFromApi() {
        new Thread(() -> {
            try {
                CarPageResponse response = carApi.getAllCarsByPages(currentPage, pageSize, "id,desc");
                totalCars = response.getTotalElements();
                isLastPage = response.isLast();
                isFirstPage = response.isFirst();
                Platform.runLater(() -> {
                    carTable.setItems(FXCollections.observableArrayList(response.getContent()));
                    updatePaginationControls();
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> showAlert("Lỗi", "Không thể tải dữ liệu từ API."));
            }
        }).start();
    }

    private void updateTableData(List<CarDTO> cars) {
        if (cars == null) {
            loadDataFromApi();
        } else {
            carTable.setItems(FXCollections.observableArrayList(cars));
        }
    }

    private void updatePaginationControls() {
        lblPage.setText("Trang " + currentPage + "/" + ((totalCars + pageSize - 1) / pageSize));
        btnPrev.setDisable(isFirstPage);
        btnNext.setDisable(isLastPage);
    }

    @FXML
    private void goToPreviousPage() {
        if (!isFirstPage) {
            currentPage--;
            loadDataFromApi();
        }
    }

    @FXML
    private void goToNextPage() {
        if (!isLastPage) {
            currentPage++;
            loadDataFromApi();
        }
    }

    @FXML
    private void handleAddCar() {
        actionHandler.handleAddCar();
    }

    @FXML
    private void handleEditCar() {
        actionHandler.handleEditCar();
    }

    @FXML
    public void handleViewDetail(){
        actionHandler.handleViewDetailCar();
    }


    @FXML
    private void handleDeleteCar() {
        actionHandler.handleDeleteCar();
    }

    @FXML
    private void handleSearchCar() {
        String searchTerm = searchField.getText();
        actionHandler.handleSearchCar(searchTerm);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}