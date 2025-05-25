package com.vinfast.controller;

import com.vinfast.api.InventoryApi;
import com.vinfast.dto.InventoryDTO;
import com.vinfast.model.InventoryPageResponse;
import com.vinfast.ui.inventory.InventoryActionHandler;
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
    private Button btnSearch;

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
    private InventoryApi inventoryApi;
    private InventoryActionHandler inventoryActionHandler;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inventoryActionHandler = new InventoryActionHandler(inventoryTable, this::updateTableData);
        inventoryApi = new InventoryApi();

        InventoryTableConfigurer tableConfigurer = new InventoryTableConfigurer(
                inventoryTable, idCol, nameCol, locationCol, capacityCol, carCount, statusCol);
        tableConfigurer.configureSize();
        tableConfigurer.configureStyle();
        tableConfigurer.setDataIntoTableView();
        loadDataFromApi();

        btnSearch.setOnAction(event -> searchInventoryByName());
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
    private void handleAddInventory() {
        inventoryActionHandler.handleAdd();
    }

    @FXML
    private void handleEditInventory() {
        inventoryActionHandler.handleEditInventory();
    }


    @FXML
    private void handleDeleteInventory() {
        inventoryActionHandler.handleDeleteInventory();
    }

    @FXML
    private void handleRefreshInventory() {
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


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void searchInventoryByName() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            showAlert("Thông báo", "Vui lòng nhập từ khóa để tìm kiếm.");
            return;
        }

        new Thread(() -> {
            try {
                // Gọi API tìm kiếm theo tên (để trống phần gọi API theo yêu cầu)
                List<InventoryDTO> searchResults = inventoryApi.searchInventoryByName(keyword);

                Platform.runLater(() -> {
                    if (searchResults != null && !searchResults.isEmpty()) {
                        inventoryTable.setItems(FXCollections.observableArrayList(searchResults));
                    } else {
                        inventoryTable.setItems(FXCollections.observableArrayList());
                        showAlert("Kết quả", "Không tìm thấy kết quả phù hợp.");
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> showAlert("Lỗi", "Không thể thực hiện tìm kiếm."));
            }
        }).start();
    }
}
