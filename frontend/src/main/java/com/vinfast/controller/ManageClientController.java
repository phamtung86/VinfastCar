package com.vinfast.controller;

import com.vinfast.api.CustomerApi;
import com.vinfast.dto.CustomerDTO;
import com.vinfast.ui.customer.CustomerActionHandler;
import com.vinfast.ui.customer.DetailView;
import com.vinfast.util.FormatUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ManageClientController implements Initializable {
    @FXML private TableView<CustomerDTO> customerTable;
    @FXML private TableColumn<CustomerDTO, Long> colId;
    @FXML private TableColumn<CustomerDTO, String> colName;
    @FXML private TableColumn<CustomerDTO, String> colPhone;
    @FXML private TableColumn<CustomerDTO, String> colEmail;
    @FXML private TableColumn<CustomerDTO, String> colAddress;
    @FXML private TableColumn<CustomerDTO, String> colCreatedAt;
    @FXML private TableColumn<CustomerDTO, String> colOrderCount;

    @FXML private Button btnPrev;
    @FXML private Button btnNext;
    @FXML private Label lblPage;

    private int page = 0;
    private final  int sizePage = 10;

    @FXML
    private Pane boxFeatureSearch;

    private final DetailView viewDetail = new DetailView();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customerTable.setPlaceholder(new Label("Đang tải dữ liệu khách hàng"));
        setTableView();
        setColumn();
        getData();
        loadAPI();
    }

    private void loadAPI(){
        new Thread(() -> {
            try {
                List<CustomerDTO> customers = CustomerApi.getCustomersByPage(page, sizePage);
                Platform.runLater(() -> {
                    customerTable.setItems(FXCollections.observableArrayList(customers));
                    lblPage.setText("Trang: " + (page + 1));
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void setTableView(){
        colId.setCellValueFactory(cell -> new SimpleLongProperty(cell.getValue().getId()).asObject());
        colName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        colPhone.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPhone()));
        colEmail.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmail()));
        colAddress.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAddress()));

        colCreatedAt.setCellValueFactory(cell ->
                new SimpleStringProperty(FormatUtils.formatDateVietnamese(cell.getValue().getCreatedAt()))
        );

        colOrderCount.setCellValueFactory(cell -> {
            int orderCount = cell.getValue().getOrders() != null ? cell.getValue().getOrders().size() : 0;
            return new SimpleStringProperty(String.valueOf(orderCount));
        });

        customerTable.setRowFactory(tv -> {
            TableRow<CustomerDTO> row = new TableRow<>();

            // Kiểm tra nếu hàng có dữ liệu (không phải là hàng trống)
            row.setOnMouseEntered(event -> {
                if (!row.isEmpty()) {  // Nếu hàng không trống
                    row.setStyle("-fx-cursor: hand;");
                }
            });

            // Đặt lại kiểu con trỏ khi chuột rời khỏi hàng
            row.setOnMouseExited(event -> {
                if (!row.isEmpty()) {  // Nếu hàng không trống
                    row.setStyle("-fx-cursor: default;");
                }
            });

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    showCustomerDetail();
                }
            });

            customerTable.setPlaceholder(new Label("Đang tải danh sách khách hàng"));

            return row;
        });
    }

    public void setColumn(){
        customerTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        for (TableColumn<?, ?> column : customerTable.getColumns()) {
            column.setMaxWidth(1f * Integer.MAX_VALUE);
        }

        colId.setStyle("-fx-alignment: CENTER;");
        colName.setStyle("-fx-alignment: CENTER;");
        colPhone.setStyle("-fx-alignment: CENTER;");
        colEmail.setStyle("-fx-alignment: CENTER;");
        colAddress.setStyle("-fx-alignment: CENTER;");
        colCreatedAt.setStyle("-fx-alignment: CENTER;");
        colOrderCount.setStyle("-fx-alignment: CENTER;");
    }

    public void getData(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        colCreatedAt.setCellValueFactory(cell -> {
            Date createdAt = cell.getValue().getCreatedAt();
            String formattedDate = FormatUtils.formatDateVietnamese(createdAt);
            return new SimpleStringProperty(formattedDate);
        });

        colOrderCount.setCellValueFactory(cell -> {
            int orderCount = cell.getValue().getOrders() != null ? cell.getValue().getOrders().size() : 0;
            return new SimpleStringProperty(String.valueOf(orderCount));
        });
    }

    @FXML
    private void handleSearch() {
        String searchTerm = ((TextField) boxFeatureSearch.getChildren().get(0)).getText();
        CustomerActionHandler.searchCustomers(searchTerm, customerTable);
    }

    @FXML
    private void handleAdd() {
        CustomerDTO newCustomer = CustomerActionHandler.addCustomer();

        if (newCustomer != null) {
            try {
                CustomerApi.addCustomer(newCustomer);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Thêm khách hàng thành công!");
                alert.showAndWait();

                customerTable.getItems().clear();
                CustomerActionHandler.loadAllCustomers(customerTable);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Lỗi khi thêm khách hàng: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void handleUpdate() {
        CustomerDTO selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng chọn khách hàng cần cập nhật!");
            alert.showAndWait();
            return;
        }

        boolean isUpdated = CustomerActionHandler.updateCustomer(selectedCustomer.getId());

        if (isUpdated) {
            customerTable.getItems().clear();
            CustomerActionHandler.loadAllCustomers(customerTable);
        }
    }

    @FXML
    private void handleDelete() {
        CustomerActionHandler.deleteCustomer(customerTable);
    }

    @FXML
    private void handleRefresh() {
        ((TextField) boxFeatureSearch.getChildren().get(0)).clear();
        customerTable.getItems().clear();
        CustomerActionHandler.loadAllCustomers(customerTable);
    }

    private void showCustomerDetail() {
        viewDetail.showDetail(customerTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void previousPage() {
        if (page > 0) {
            page--;
            loadAPI();
        }

    }

    @FXML
    private void nextPage() {
        page++;
        new Thread(() -> {
            List<CustomerDTO> customers = CustomerApi.getCustomersByPage(page, sizePage);
            if (!customers.isEmpty()) {
                Platform.runLater(() -> {
                    customerTable.setItems(FXCollections.observableArrayList(customers));
                    lblPage.setText("Trang: " + (page + 1));
                });
            } else {
                page--; // Không có dữ liệu → lùi lại
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Không còn trang tiếp theo.");
                    alert.showAndWait();
                });
            }
        }).start();
    }
}