package com.vinfast.controller;

import com.vinfast.dto.CustomerDTO;
import com.vinfast.dto.CarDTO;
import com.vinfast.dto.OrderDTO;
import com.vinfast.util.FormatUtils;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerDetailController implements Initializable {

    // TextFields for customer information
    @FXML private TextField txtCustomerId;
    @FXML private TextField txtCustomerName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private TextField txtAddress;

    // TextFields for car information
    @FXML private TextField txtVehicleId;
    @FXML private TextField txtVehicleName;
    @FXML private TextField txtVehiclePrice;

    // TableView and columns
    @FXML private TableView<OrderDTO> tableView;
    @FXML private TableColumn<OrderDTO, Integer> colSerial;
    @FXML private TableColumn<OrderDTO, String> colCustomerName;
    @FXML private TableColumn<OrderDTO, String> colPhone;
    @FXML private TableColumn<OrderDTO, String> colEmail;
    @FXML private TableColumn<OrderDTO, String> colAddress;
    @FXML private TableColumn<OrderDTO, Integer> colIdCar;
    @FXML private TableColumn<OrderDTO, String> colPurchaseDate;
    @FXML private TableColumn<OrderDTO, String> colPrice;

    // Data list for TableView
    private ObservableList<OrderDTO> orderList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setTable();
        selectRow();
        setColumn();
    }

    public void setTable() {
        colSerial.setCellValueFactory(cellData -> new SimpleIntegerProperty(
                orderList.indexOf(cellData.getValue()) + 1).asObject());
        colCustomerName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCustomer() != null ? cellData.getValue().getCustomer().getName() : ""));
        colPhone.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCustomer() != null ? cellData.getValue().getCustomer().getPhone() : ""));
        colEmail.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCustomer() != null ? cellData.getValue().getCustomer().getEmail() : ""));
        colAddress.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCustomer() != null ? cellData.getValue().getCustomer().getAddress() : ""));
        colIdCar.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getCar() != null ? cellData.getValue().getCar().getId() : 0).asObject());
        colPurchaseDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getOrderDate() != null
                        ? FormatUtils.formatDateVietnamese(cellData.getValue().getOrderDate())
                        : "")
        );
        colPrice.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getCar() != null
                                ? FormatUtils.formatCurrency(BigDecimal.valueOf(cellData.getValue().getCar().getPrice()))
                                : ""
                )
        );

        // Bind data to TableView
        tableView.setItems(orderList);
    }

    public void selectRow() {
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && newSelection.getCar() != null) {
                CarDTO car = newSelection.getCar();
                txtVehicleId.setText(String.valueOf(car.getId()));
                txtVehicleName.setText(car.getName());
                txtVehiclePrice.setText(String.valueOf(car.getPrice()));
            } else {
                txtVehicleId.clear();
                txtVehicleName.clear();
                txtVehiclePrice.clear();
            }
        });

        tableView.setRowFactory(tv -> {
            TableRow<OrderDTO> row = new TableRow<>();

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

            return row;
        });

    }

    public void setColumn() {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        for (TableColumn<?, ?> column : tableView.getColumns()) {
            column.setMaxWidth(1f * Integer.MAX_VALUE);
        }

        colSerial.setStyle("-fx-alignment: CENTER;");
        colCustomerName.setStyle("-fx-alignment: CENTER;");
        colPhone.setStyle("-fx-alignment: CENTER;");
        colEmail.setStyle("-fx-alignment: CENTER;");
        colAddress.setStyle("-fx-alignment: CENTER;");
        colIdCar.setStyle("-fx-alignment: CENTER;");
        colPurchaseDate.setStyle("-fx-alignment: CENTER;");
        colPrice.setStyle("-fx-alignment: CENTER;");
    }


    public void setCustomerDetail(CustomerDTO customer) {
        if (customer == null) return;

        // Set customer info
        txtCustomerId.setText(String.valueOf(customer.getId()));
        txtCustomerName.setText(customer.getName());
        txtEmail.setText(customer.getEmail());
        txtPhone.setText(customer.getPhone());
        txtAddress.setText(customer.getAddress());

        // Load orders
        orderList.clear();
        if (customer.getOrders() != null) {
            orderList.addAll(customer.getOrders());
        }

        // Select first order by default to show car info
        if (!orderList.isEmpty()) {
            tableView.getSelectionModel().selectFirst();
        } else {
            txtVehicleId.clear();
            txtVehicleName.clear();
            txtVehiclePrice.clear();
            // Hiển thị thông báo
            tableView.setPlaceholder(new javafx.scene.control.Label("Khách hàng chưa có đơn hàng."));
        }
    }
}
