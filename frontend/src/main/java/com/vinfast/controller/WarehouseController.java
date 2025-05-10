package com.vinfast.controller;

import com.vinfast.api.CarApi;
import com.vinfast.api.InventoryApi;
import com.vinfast.dto.InventoryDTO;
import com.vinfast.model.InventoryPageResponse;
import com.vinfast.ui.car.CarActionHandler;
import com.vinfast.ui.table.InventoryTableConfigurer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class WarehouseController implements Initializable {

    @FXML
    private TableView<InventoryDTO> inventoryTable;

    @FXML
    private TableColumn<InventoryDTO, Integer> idCol;

    @FXML
    private TableColumn<InventoryDTO, String> nameCol;

    @FXML
    private TableColumn<InventoryDTO, Integer> capacityCol;

    @FXML
    private TableColumn<InventoryDTO, String> locationCol;

    @FXML
    private TableColumn<InventoryDTO, Integer> carCount;

    @FXML
    private TableColumn<InventoryDTO, Float> statusCol;


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
    private long totalInventory = 0;
    private boolean isLastPage = false;
    private boolean isFirstPage = true;
    private CarActionHandler actionHandler;
    private CarApi carApi;
    private InventoryApi inventoryApi;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        actionHandler = new CarActionHandler(inventoryTable, this::updateTableData);
        inventoryApi = new InventoryApi();

        InventoryTableConfigurer tableConfigurer = new InventoryTableConfigurer(
                inventoryTable, idCol, nameCol, locationCol, capacityCol, carCount, statusCol);
        tableConfigurer.configureSize();
        tableConfigurer.configureStyle();
        tableConfigurer.setDataIntoTableView();
        loadDataFromApi();
    }

    private void loadDataFromApi() {
        new Thread(() -> {
            try {
                InventoryPageResponse response = inventoryApi.getInventoryByPages(currentPage, pageSize, "id,desc");
                totalInventory = response.getTotalElements();
                isLastPage = response.isLast();
                isFirstPage = response.isFirst();
                Platform.runLater(() -> {
                    inventoryTable.setItems(FXCollections.observableArrayList(response.getContent()));
                    updatePaginationControls();
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> showAlert("Lỗi", "Không thể tải dữ liệu từ API."));
            }
        }).start();
    }

    private void updateTableData(List<InventoryDTO> cars) {
        if (cars == null) {
            loadDataFromApi();
        } else {
//            carTable.setItems(FXCollections.observableArrayList(cars));
        }
    }

    private void updatePaginationControls() {
        lblPage.setText("Trang " + currentPage + "/" + ((totalInventory + pageSize - 1) / pageSize));
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
